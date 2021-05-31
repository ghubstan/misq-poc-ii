package org.misq.reactive.operators;

import io.reactivex.Observable;

import static java.lang.System.out;

/**
 * CombineLatest - Combine the latest item emitted by each Observable via a specified function and emit resulted item.
 */
public class CombiningOperatorDemo {
    public static void main(String[] args) {
        String[] letters = {"a", "b", "c", "d", "e", "f", "g"};
        Integer[] numbers = {1, 2, 3, 4, 5, 6};
        final StringBuilder result = new StringBuilder();
        Observable<String> observableA = Observable.fromArray(letters);
        Observable<Integer> observableB = Observable.fromArray(numbers);
        Observable.combineLatest(observableA, observableB, (a, b) -> a + b)
                .subscribe(letter -> result.append(letter));
        out.println(result);
    }
}
