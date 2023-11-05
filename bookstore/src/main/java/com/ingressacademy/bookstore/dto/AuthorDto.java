package com.ingressacademy.bookstore.dto;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class AuthorDto {
    private String name;

    public AuthorDto(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
