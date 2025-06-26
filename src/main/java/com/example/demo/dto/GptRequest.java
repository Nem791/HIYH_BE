package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

@Setter
@Getter
public class GptRequest {
    private List<GptMessage> messages;
    private Map<String, Object> response_format; // Leave as Map for flexibility

}
