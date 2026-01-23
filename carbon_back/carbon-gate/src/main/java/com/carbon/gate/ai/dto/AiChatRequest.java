package com.carbon.gate.ai.dto;

import java.util.ArrayList;
import java.util.List;

public class AiChatRequest {
    private List<ChatMessage> messages = new ArrayList<>();
    private String model;
    private Double temperature;
    private Boolean stream = true;

    public List<ChatMessage> getMessages() {
        return messages;
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public Double getTemperature() {
        return temperature;
    }

    public void setTemperature(Double temperature) {
        this.temperature = temperature;
    }

    public Boolean getStream() {
        return stream;
    }

    public void setStream(Boolean stream) {
        this.stream = stream;
    }
}

