package org.misq.reactive.subject;

import io.reactivex.subjects.AsyncSubject;

import static java.lang.System.out;

/**
 * AsyncSubject emits the only last value followed by a completion event or the received error to Observers.
 */
public class AsyncSubjectDemo {
    public static void main(String[] args) {
        final StringBuilder result1 = new StringBuilder();
        final StringBuilder result2 = new StringBuilder();

        AsyncSubject<String> subject = AsyncSubject.create();
        subject.subscribe(value -> result1.append(value));
        subject.onNext("a");
        subject.onNext("b");
        subject.onNext("c");
        subject.subscribe(value -> result2.append(value));
        subject.onNext("d");
        subject.onComplete();

        //Output will be d being the last item emitted
        out.println(result1);
        //Output will be d being the last item emitted
        out.println(result2);
    }
}
