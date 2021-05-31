package org.misq.reactive.operators;

import io.reactivex.Observable;

/**
 * Emits only those items which pass the given predicate function.
 */
public class FilteringOperatorDemo {
    public static void main(String[] args) {
        String[] letters = {"a", "b", "c", "d", "e", "f", "g"};
        final StringBuilder result = new StringBuilder();
        Observable<String> observable = Observable.fromArray(letters);
        observable.take(2)
                .subscribe(letter -> result.append(letter));
        System.out.println(result);
    }
}
