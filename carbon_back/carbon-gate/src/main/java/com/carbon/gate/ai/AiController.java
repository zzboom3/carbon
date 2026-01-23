package com.carbon.gate.ai;

import cn.hutool.core.util.StrUtil;
import com.carbon.common.utils.HttpContextUtils;
import com.carbon.common.utils.JwtUtil;
import com.carbon.domain.common.ApiResult;
import com.carbon.gate.ai.dto.AiChatRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletResponse;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/ai")
public class AiController {
    private final AiClient aiClient;
    private final AiProperties aiProperties;

    public AiController(AiClient aiClient, AiProperties aiProperties) {
        this.aiClient = aiClient;
        this.aiProperties = aiProperties;
    }

    @PostMapping(value = "/chat/stream", produces = "text/event-stream;charset=UTF-8")
    public void chatStream(@RequestBody AiChatRequest request, HttpServletResponse response) throws Exception {
        response.setCharacterEncoding(StandardCharsets.UTF_8.name());
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("Connection", "keep-alive");

        String token = HttpContextUtils.getRequestToken();
        if (!JwtUtil.isValidToken(token)) {
            response.getWriter().write("event: error\ndata: " + escapeSse("登录状态已过期，请重新登录") + "\n\n");
            response.getWriter().write("data: [DONE]\n\n");
            response.getWriter().flush();
            return;
        }

        // Inject System Prompt
        String systemPrompt = "你是一个精通 Carbon Management System (Carbon Official) 的智能助手。\n" +
                "【项目背景】\n" +
                "这是一个基于 Java Spring Cloud 的碳资产管理与交易平台，包含碳资产管理、碳交易、区块链存证等核心功能。\n" +
                "【技术栈】\n" +
                "- 后端：Java 8, Spring Boot 2.3.2, Spring Cloud Hoxton.SR8, Alibaba Cloud 2.2.5\n" +
                "- 中间件：RocketMQ (2.1.0), Redis, Nacos (注册/配置), Elasticsearch (7.6.2), MySQL (MyBatis-Plus)\n" +
                "- 区块链：ChainMaker (长安链), Web3j\n" +
                "【核心模块】\n" +
                "1. carbon-assets: 碳资产管理。核心实体：CarbonProject(减排项目), CarbonAssets(中和资产), CarbonMetaregistry(项目注册表)\n" +
                "2. carbon-trade: 碳交易。核心实体：CarbonTradeQuote(行情报价), CarbonTradeContract(履约合同)\n" +
                "3. carbon-chainmaker: 区块链交互。负责资产上链、核证、交易存证\n" +
                "4. carbon-system: 系统管理。实体：SysUser, SysRole, SysMenu, SysOrg\n" +
                "5. carbon-mq: 消息消费者。处理异步业务（如立项审批、交易撮合）\n" +
                "【数据库关键表】\n" +
                "- sys_user, sys_role, sys_menu (RBAC)\n" +
                "- carbon_project, carbon_assets (资产业务)\n" +
                "- carbon_trade_quote, carbon_trade_contract (交易业务)\n" +
                "- tab_order, tab_carbon_detail (出行/积分业务)\n" +
                "请基于以上信息回答用户关于代码、业务或架构的问题。如果用户询问代码位置，请根据包结构（如 com.carbon.assets...）进行推断。";

        if (request.getMessages() == null) {
            request.setMessages(new ArrayList<>());
        }
        // Avoid adding duplicate system prompt if frontend sends one (though unlikely here)
        boolean hasSystem = request.getMessages().stream().anyMatch(m -> "system".equals(m.getRole()));
        if (!hasSystem) {
            request.getMessages().add(0, new com.carbon.gate.ai.dto.ChatMessage("system", systemPrompt));
        }

        try {
            aiClient.streamChat(request, delta -> {
                try {
                    String payload = "data: " + escapeSse(delta) + "\n\n";
                    response.getWriter().write(payload);
                    response.getWriter().flush();
                } catch (Exception ignored) {
                }
            });
            response.getWriter().write("data: [DONE]\n\n");
            response.getWriter().flush();
        } catch (Exception e) {
            String msg = StrUtil.blankToDefault(e.getMessage(), "AI 调用失败");
            String payload = "event: error\ndata: " + escapeSse(msg) + "\n\n";
            response.getWriter().write(payload);
            response.getWriter().flush();
        }
    }

    @PostMapping("/health")
    public ApiResult<Boolean> health() {
        return ApiResult.ok(true);
    }

    @PostMapping("/config")
    public ApiResult<Map<String, Object>> config() {
        Map<String, Object> cfg = new LinkedHashMap<>();
        cfg.put("baseUrl", aiProperties.getBaseUrl());
        cfg.put("chatPath", aiProperties.getChatPath());
        cfg.put("model", aiProperties.getModel());
        cfg.put("connectTimeoutSeconds", aiProperties.getConnectTimeoutSeconds());
        cfg.put("readTimeoutSeconds", aiProperties.getReadTimeoutSeconds());
        cfg.put("apiKeyConfigured", StringUtils.hasText(aiProperties.getApiKey()));
        cfg.put("apiKeyLength", aiProperties.getApiKey() == null ? 0 : aiProperties.getApiKey().length());
        cfg.put("apiKeyFingerprint", fingerprint(aiProperties.getApiKey()));
        return ApiResult.ok(cfg);
    }

    @PostMapping("/upstream/check")
    public ApiResult<Map<String, Object>> upstreamCheck() {
        String token = HttpContextUtils.getRequestToken();
        if (!JwtUtil.isValidToken(token)) {
            return ApiResult.fail(401, "登录状态已过期，请重新登录");
        }

        AiChatRequest req = new AiChatRequest();
        req.setModel(aiProperties.getModel());
        req.setStream(false);
        List<com.carbon.gate.ai.dto.ChatMessage> msgs = new ArrayList<>();
        msgs.add(new com.carbon.gate.ai.dto.ChatMessage("user", "ping"));
        req.setMessages(msgs);

        Map<String, Object> result = new LinkedHashMap<>();
        result.put("baseUrl", aiProperties.getBaseUrl());
        result.put("chatPath", aiProperties.getChatPath());
        result.put("model", aiProperties.getModel());
        result.put("apiKeyFingerprint", fingerprint(aiProperties.getApiKey()));
        try {
            StringBuilder sb = new StringBuilder();
            aiClient.streamChat(req, sb::append);
            result.put("ok", true);
            result.put("replyPreview", sb.length() > 200 ? sb.substring(0, 200) : sb.toString());
            return ApiResult.ok(result);
        } catch (Exception e) {
            String msg = StringUtils.hasText(e.getMessage()) ? e.getMessage() : "上游调用失败";
            result.put("ok", false);
            result.put("error", msg.length() > 400 ? msg.substring(0, 400) : msg);
            return ApiResult.ok(result);
        }
    }

    private String fingerprint(String apiKey) {
        if (!StringUtils.hasText(apiKey)) {
            return "";
        }
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] digest = md.digest(apiKey.getBytes(StandardCharsets.UTF_8));
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < digest.length && i < 8; i++) {
                sb.append(String.format("%02x", digest[i]));
            }
            return sb.toString();
        } catch (Exception ignored) {
            return "";
        }
    }

    private String escapeSse(String s) {
        if (s == null) {
            return "";
        }
        return s.replace("\r", "").replace("\n", "\\n");
    }
}
