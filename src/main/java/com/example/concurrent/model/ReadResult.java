package com.example.concurrent.model;

import lombok.Getter;
import lombok.Setter;

@Getter
public class ReadResult {
    private int a;
    private long b;
    private String c;

    public ReadResult(int a, long b, String c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }
}
