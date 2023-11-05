package com.ingressacademy.bookstore.model;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum Permission {

    AUTHOR_READ("author:read"),
    AUTHOR_UPDATE("author:update"),
    AUTHOR_CREATE("author:create"),
    AUTHOR_DELETE("author:delete"),
    STUDENT_READ("student:read"),
    ;

    @Getter
    private final String permission;
}
