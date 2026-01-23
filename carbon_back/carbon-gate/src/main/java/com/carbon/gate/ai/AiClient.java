package com.carbon.gate.ai;

import com.carbon.gate.ai.dto.AiChatRequest;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Component
public class AiClient {
    private final AiProperties props;
    private final ObjectMapper objectMapper;

    public AiClient(AiProperties props, ObjectMapper objectMapper) {
        this.props = props;
        this.objectMapper = objectMapper;
    }

    public void streamChat(AiChatRequest request, Consumer<String> onDelta) throws Exception {
        if (!StringUtils.hasText(props.getApiKey())) {
            throw new IllegalStateException("AI 服务未配置");
        }

        Map<String, Object> payload = new HashMap<>();
        payload.put("model", StringUtils.hasText(request.getModel()) ? request.getModel() : props.getModel());
        payload.put("messages", request.getMessages());
        payload.put("stream", request.getStream() == null ? true : request.getStream());
        if (request.getTemperature() != null) {
            payload.put("temperature", request.getTemperature());
        }

        String body = objectMapper.writeValueAsString(payload);

        URI uri = buildUri(props.getBaseUrl(), props.getChatPath());
        HttpURLConnection conn = (HttpURLConnection) new URL(uri.toString()).openConnection();
        conn.setConnectTimeout(Math.max(1000, props.getConnectTimeoutSeconds() * 1000));
        conn.setReadTimeout(Math.max(1000, props.getReadTimeoutSeconds() * 1000));
        conn.setRequestMethod("POST");
        conn.setDoOutput(true);
        conn.setRequestProperty("Content-Type", "application/json");
        conn.setRequestProperty("Accept", "text/event-stream");
        conn.setRequestProperty(props.getAuthHeaderName(), props.getAuthHeaderPrefix() + props.getApiKey());

        byte[] bytes = body.getBytes(StandardCharsets.UTF_8);
        conn.getOutputStream().write(bytes);
        conn.getOutputStream().flush();

        int code = conn.getResponseCode();
        InputStream stream = (code >= 200 && code < 300) ? conn.getInputStream() : conn.getErrorStream();
        String contentType = conn.getHeaderField("Content-Type");

        if (code < 200 || code >= 300) {
            String err = readAll(stream);
            throw new IllegalStateException(err);
        }

        if (contentType != null && contentType.toLowerCase().contains("text/event-stream")) {
            try (BufferedReader br = new BufferedReader(new InputStreamReader(stream, StandardCharsets.UTF_8))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (!line.startsWith("data:")) {
                        continue;
                    }
                    String data = line.substring(5).trim();
                    if (data.isEmpty()) {
                        continue;
                    }
                    if ("[DONE]".equals(data)) {
                        break;
                    }
                    String delta = extractOpenAiDelta(data);
                    if (StringUtils.hasText(delta)) {
                        onDelta.accept(delta);
                    }
                }
            }
            return;
        }

        String json = readAll(stream);
        String full = extractOpenAiFullContent(json);
        if (StringUtils.hasText(full)) {
            onDelta.accept(full);
            return;
        }
        throw new IllegalStateException(json);
    }

    private String extractOpenAiDelta(String json) {
        try {
            JsonNode node = objectMapper.readTree(json);
            JsonNode choices = node.path("choices");
            if (choices.isArray() && choices.size() > 0) {
                JsonNode delta = choices.get(0).path("delta");
                JsonNode content = delta.path("content");
                if (!content.isMissingNode() && !content.isNull()) {
                    return content.asText();
                }
            }
        } catch (Exception ignored) {
        }
        return null;
    }

    private String extractOpenAiFullContent(String json) {
        try {
            JsonNode node = objectMapper.readTree(json);
            JsonNode choices = node.path("choices");
            if (!choices.isArray() || choices.size() == 0) {
                return null;
            }
            JsonNode c0 = choices.get(0);
            JsonNode messageContent = c0.path("message").path("content");
            if (!messageContent.isMissingNode() && !messageContent.isNull()) {
                return messageContent.asText();
            }
            JsonNode text = c0.path("text");
            if (!text.isMissingNode() && !text.isNull()) {
                return text.asText();
            }
            return null;
        } catch (Exception ignored) {
            return null;
        }
    }

    private String readAll(InputStream inputStream) throws Exception {
        if (inputStream == null) {
            return "";
        }
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buf = new byte[4096];
        int n;
        while ((n = inputStream.read(buf)) > 0) {
            bos.write(buf, 0, n);
        }
        return bos.toString(StandardCharsets.UTF_8.name());
    }

    private URI buildUri(String baseUrl, String chatPath) {
        String base = normalizeBaseUrl(baseUrl);
        String path = normalizePath(chatPath);
        if (base.endsWith("/v1") && path.startsWith("/v1/")) {
            path = path.substring(3);
        }
        return URI.create(base + path);
    }

    private String normalizeBaseUrl(String baseUrl) {
        if (!StringUtils.hasText(baseUrl)) {
            return "";
        }
        return baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
    }

    private String normalizePath(String path) {
        if (!StringUtils.hasText(path)) {
            return "";
        }
        return path.startsWith("/") ? path : ("/" + path);
    }
}
