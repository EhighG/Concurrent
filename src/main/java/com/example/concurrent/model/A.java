package com.example.concurrent.model;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class A {
    private int a;
    private long b;
    private String c;

    public A(int a, long b, String c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public static A createWithZero() {
        return new A(0, 0L, "0");
    }

    public void increase() {
        this.a++;
        this.b++;
        this.c = Integer.parseInt(this.c) + 1 + "";
    }
}