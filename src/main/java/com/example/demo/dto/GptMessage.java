package com.example.demo.dto;

import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class GptMessage {
    private String role;
    private String content;

    public GptMessage() { }
    public GptMessage(String role, String content) {
        this.role = role;
        this.content = content;
    }

}