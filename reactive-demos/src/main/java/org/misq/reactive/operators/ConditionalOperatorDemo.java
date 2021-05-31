package org.misq.reactive.operators;

import io.reactivex.Observable;

import static java.lang.System.out;

/**
 * Use defaultIfEmpty operator to operate on an Observable.
 */
public class ConditionalOperatorDemo {
    public static void main(String[] args) {
        final StringBuilder result = new StringBuilder();
        Observable.empty().defaultIfEmpty("No Data")
                .subscribe(s -> result.append(s));
        out.println(result);
        String[] letters = {"a", "b", "c", "d", "e", "f", "g"};
        final StringBuilder result1 = new StringBuilder();
        Observable.fromArray(letters)
                .firstElement()
                .defaultIfEmpty("No data")
                .subscribe(s -> result1.append(s));
        out.println(result1);
    }
}
