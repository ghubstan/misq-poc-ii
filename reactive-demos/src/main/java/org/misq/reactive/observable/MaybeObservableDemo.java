package org.misq.reactive.observable;

import io.reactivex.Maybe;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.schedulers.Schedulers;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * The MayBe class represents deferred response. MayBe observable can emit either a single successful value or no value.
 * <p>
 * Protocol:  Following is the sequential protocol that MayBe Observable operates.
 * onSubscribe (onSuccess | onError | OnComplete)?
 */
public class MaybeObservableDemo {
    public static void main(String[] args) throws InterruptedException {
        //Create an observer
        Disposable disposable = Maybe.just("Hello World")
                .delay(2, SECONDS, Schedulers.io())
                .subscribeWith(new DisposableMaybeObserver<String>() {
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onSuccess(String value) {
                        System.out.println(value);
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
