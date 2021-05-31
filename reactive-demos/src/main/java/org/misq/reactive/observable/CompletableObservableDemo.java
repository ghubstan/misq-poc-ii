package org.misq.reactive.observable;

import io.reactivex.Completable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableCompletableObserver;
import io.reactivex.schedulers.Schedulers;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * The Completable class represents deferred response. Completable observable can either indicate a successful
 * completion or error.
 * <p>
 * Protocol:  Following is the sequential protocol that Completable Observable operates.
 * onSubscribe (onError | onComplete)?
 */
public class CompletableObservableDemo {
    public static void main(String[] args) throws InterruptedException {

        //Create an observer
        Disposable disposable = Completable.complete()
                .delay(2, SECONDS, Schedulers.io())
                .subscribeWith(new DisposableCompletableObserver() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onStart() {
                        System.out.println("Started!");
                    }

                    @Override
                    public void onComplete() {
                        System.out.println("Done!");
                    }
                });
        Thread.sleep(3000);

        // Start observing
        disposable.dispose();
    }
}
