package org.misq.reactive.scheduler;

import io.reactivex.Observable;
import io.reactivex.schedulers.Schedulers;

import java.util.Random;

import static java.lang.System.out;

/**
 * Schedulers.io() method creates and returns a Scheduler intended for IO-bound work.
 * Thread pool may extend as needed. Best for I/O intensive operations.
 */
public class SchedulersIODemo {
    public static void main(String[] args) throws InterruptedException {
        Observable.just("A", "AB", "ABC")
                .flatMap(v -> getLengthWithDelay(v)
                        .doOnNext(s -> out.println("Processing Thread " + Thread.currentThread().getName()))
                        .subscribeOn(Schedulers.io()))
                .subscribe(length -> out.println("Receiver Thread " + Thread.currentThread().getName() + ", Item length " + length));

        Thread.sleep(10000);
    }

    protected static Observable<Integer> getLengthWithDelay(String v) {
        Random random = new Random();
        try {
            Thread.sleep(random.nextInt(3) * 1000);
            return Observable.just(v.length());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return null;
    }
}

