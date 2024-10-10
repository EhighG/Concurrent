package com.example.concurrent;

import com.example.concurrent.model.A;
import com.example.concurrent.model.ReadResult;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReadWriteLock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

public class TargetService {

    private A A;
    // 공정성 유지; 특정 작업이 오래 밀리면 안됨
    private ReadWriteLock lock = new ReentrantReadWriteLock(true);
    private Lock readLock = lock.readLock();
    private Lock writeLock = lock.writeLock();

    public TargetService(A a) {
        A = a;
    }

    public ReadResult read() {
        readLock.lock();
        try {
            int a = A.getA();
            Thread.sleep(0, 1);
            long b = A.getB();
            Thread.sleep(0, 1);
            String c = A.getC();
            return new ReadResult(a, b, c);
        } catch (InterruptedException e) {

        } finally {
            readLock.unlock();
        }
        return null;
    }

    /**
     * A 안의 값을 1씩 올린다.
     */
    public void update() {
        writeLock.lock();
        A.increase();
        writeLock.unlock();
    }
}
