package org.misq.reactive.observable;

// From https://www.tutorialspoint.com/rxjava/rxjava_environment_setup.htm

import io.reactivex.Flowable;

public class FlowableJustDemo {
    public static void main(String[] args) {
        // Flowable that signals 'just' one constant and completes.
        Flowable.just("Hello World!").subscribe(System.out::println);
    }
}
