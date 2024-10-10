package com.example.concurrent;

import com.example.concurrent.model.A;
import com.example.concurrent.model.ReadResult;

public class TargetService {
    private A A;

    public TargetService(A a) {
        A = a;
    }

    public ReadResult read() {
        try {
            int a = A.getA();
            Thread.sleep(0, 1);
            long b = A.getB();
            Thread.sleep(0, 1);
            String c = A.getC();
            return new ReadResult(a, b, c);
        } catch (Exception e) {

        }
        return null;
    }

    /**
     * A 안의 값을 1씩 올린다.
     */
    public void update() {
        A.increase();
    }
}
