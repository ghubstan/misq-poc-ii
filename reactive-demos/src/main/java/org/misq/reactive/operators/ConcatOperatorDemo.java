package org.misq.reactive.operators;

import io.reactivex.Observable;

import static java.lang.System.out;

/**
 * Use concat operator to operate on multiple Observables.
 */
public class ConcatOperatorDemo {
    public static void main(String[] args) {
        Integer[] numbers = {1, 2, 3, 4, 5, 6};
        String[] letters = {"a", "b", "c", "d", "e", "f", "g"};
        final StringBuilder result = new StringBuilder();
        Observable<String> observable1 = Observable.fromArray(letters);
        Observable<Integer> observable2 = Observable.fromArray(numbers);
        Observable.concat(observable1, observable2)
                .subscribe(letter -> result.append(letter));
        out.println(result);
    }
}
