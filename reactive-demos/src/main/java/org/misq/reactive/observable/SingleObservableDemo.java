package org.misq.reactive.observable;

import io.reactivex.Single;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * The Single class represents the single value response. Single observable can only emit either a single successful
 * value or an error. It does not emit onComplete event.
 * <p>
 * Protocol:  Following is the sequential protocol that Single Observable operates.
 * onSubscribe (onSuccess | onError)?
 */
public class SingleObservableDemo {

    public static void main(String[] args) throws InterruptedException {
        // Create a Single source Observable, consumable by a SingleObserver.
        Single<String> testSingle = Single.just("Hello World");

        // Create the single source's observer.
        Disposable disposable = testSingle
                .delay(2, SECONDS, Schedulers.io())
                .subscribeWith(
                        // Subscribe with inline Observer.
                        new DisposableSingleObserver<String>() {
                            @Override
                            public void onError(Throwable e) {
                                e.printStackTrace();
                            }

                            @Override
                            public void onSuccess(String value) {
                                System.out.println(value);
                            }
                        });

        Thread.sleep(3000);

        // Start observing
        disposable.dispose();
    }
}