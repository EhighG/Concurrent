package com.example.concurrent;

import com.example.concurrent.model.A;
import com.example.concurrent.model.ReadResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

public class ConcurrentTest {

    private TargetService targetService;

    @BeforeEach
    void initTargetService() {
        targetService = new TargetService(A.createWithZero());
    }


    // ReadResult의 결과가 0, 0L, "0" 처럼 숫자값이 일치하는지 검증.
    // 불일치하는 것은 뽑아서 로그를 남기고, 성공 요청 수와 실패 요청 수를 카운트해서 출력한다.
    @Test
    @DisplayName("동시성 처리를 하지 않은 객체에 read, write 작업이 겹쳐서 발생하면, 동시성 이슈가 생긴다.")
    void concurrentReadWriteTest() throws InterruptedException {
        int numThreads = 2000;
        ExecutorService executorService = Executors.newFixedThreadPool(numThreads);
        CountDownLatch doneSignal = new CountDownLatch(numThreads);

        AtomicInteger successCnt = new AtomicInteger();
        AtomicInteger failCnt = new AtomicInteger();

        // 시간 측정
        long sTime = System.currentTimeMillis();

        for (int i = 0; i < numThreads; i++) {
            try {
                if (i % 200 != 0) { // read
                    executorService.submit(() -> {
                        ReadResult result = targetService.read();
                        boolean succeeded = compareAndAlert(result);
                        if (succeeded) successCnt.getAndIncrement();
                        else failCnt.getAndIncrement();
                    });
                } else { // write
                    executorService.submit(() -> {
                        targetService.update();
                        successCnt.getAndIncrement();
                    });
                }
            } catch (Exception e) {
                System.out.println(e.getMessage());
            } finally {
                doneSignal.countDown();
            }
        }
        doneSignal.await(); // 카운트가 0이 될 때까지 기다린다.
        executorService.shutdown();

        // 결과
        System.out.println("총 횟수 : " + numThreads + ", 성공 : " + successCnt.get()
                + ", 실패 : " + failCnt.get());

        long eTime = System.currentTimeMillis();
        long elapsedTime = eTime - sTime;
        long elapsedTimeSecond = elapsedTime / 1000;
        long elapsedTimeMillis = elapsedTime % 1000;

        System.out.println("소요 시간 : " + elapsedTimeSecond + "초 " + elapsedTimeMillis + " ms");
    }

    private boolean compareAndAlert(ReadResult result) {
        int a = result.getA();
        long b = result.getB();
        int cValue = Integer.parseInt(result.getC());
        if (a == b && b == cValue) {
            return true;
        }
        System.out.println("bad result. a = " + a + ", b = " + b + ", cValue = " + cValue);
        return false;
    }
}
