package com.example.tugasakhir.util;

public enum StatusCode {
    SUCCESS("200"),
    BAD_REQUEST("400"),
    NOT_FOUND("404"),
    CONFLICT("409");

    private final String value;

    StatusCode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
