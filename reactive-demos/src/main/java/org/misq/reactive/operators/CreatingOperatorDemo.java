package org.misq.reactive.operators;

import io.reactivex.Observable;

import static java.lang.System.out;

/**
 * Creates an Observable from scratch and allows observer method to call programmatically.
 */
public class CreatingOperatorDemo {
    public static void main(String[] args) {
        String[] letters = {"a", "b", "c", "d", "e", "f", "g"};
        final StringBuilder result = new StringBuilder();
        Observable<String> observable = Observable.fromArray(letters);
        observable.map(String::toUpperCase)
                .subscribe(letter -> result.append(letter));
        out.println(result);
    }
}
