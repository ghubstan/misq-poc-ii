package org.misq.reactive.operators;

import io.reactivex.Observable;
import io.reactivex.observables.ConnectableObservable;

import static java.lang.System.out;

/**
 * Use connect operator on a ConnectableObservable.
 */
public class ConnectableOperatorDemo {
    public static void main(String[] args) {
        String[] letters = {"a", "b", "c", "d", "e", "f", "g"};
        final StringBuilder result = new StringBuilder();
        ConnectableObservable<String> connectable = Observable.fromArray(letters).publish();
        connectable.subscribe(letter -> result.append(letter));
        out.println(result.length());   // 0

        connectable.connect();
        out.println(result.length());   // 7
        out.println(result);            // abcdefg
    }
}
