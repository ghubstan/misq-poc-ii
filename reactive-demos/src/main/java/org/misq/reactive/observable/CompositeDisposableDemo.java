package org.misq.reactive.observable;

import io.reactivex.Maybe;
import io.reactivex.Single;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.observers.DisposableMaybeObserver;
import io.reactivex.observers.DisposableSingleObserver;
import io.reactivex.schedulers.Schedulers;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * The CompositeDisposable class represents a container which can hold multiple disposables and offers O(1) complexity
 * of adding and removing disposables.
 */
public class CompositeDisposableDemo {
    public static void main(String[] args) throws InterruptedException {
        CompositeDisposable compositeDisposable = new CompositeDisposable();

        // Create a Single observer
        Disposable disposableSingle = Single.just("Hello World (1)")
                .delay(2, SECONDS, Schedulers.io())
                .subscribeWith(
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

        // Create a maybe observer
        Disposable disposableMayBe = Maybe.just("Hi (2)")
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

        compositeDisposable.add(disposableSingle);
        compositeDisposable.add(disposableMayBe);

        // Start observing.
        compositeDisposable.dispose();
    }
}
