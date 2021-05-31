package org.misq.reactive.operators;

import io.reactivex.Observable;

import static java.lang.System.out;

/**
 * Use subscribe operator to subscribe to an Observable.
 */
public class UtilityOperatorDemo {
    public static void main(String[] args) {
        String[] letters = {"a", "b", "c", "d", "e", "f", "g"};
        final StringBuilder result = new StringBuilder();
        Observable<String> observable = Observable.fromArray(letters);
        observable.subscribe(letter -> result.append(letter));
        out.println(result);
    }
}
