package com.example.demo.dto;

import java.util.List;
import java.util.Map;

public class GptRequest {
    private List<GptMessage> messages;
    private Map<String, Object> response_format; // Leave as Map for flexibility

    public List<GptMessage> getMessages() { return messages; }
    public void setMessages(List<GptMessage> messages) { this.messages = messages; }

    public Map<String, Object> getResponse_format() { return response_format; }
    public void setResponse_format(Map<String, Object> response_format) { this.response_format = response_format; }
}
