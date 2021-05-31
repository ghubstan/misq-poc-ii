package org.misq.reactive.subject;

import io.reactivex.subjects.PublishSubject;

import static java.lang.System.out;

/**
 * PublishSubject emits items to currently subscribed Observers and terminal events to current or late Observers.
 */
public class PublishSubjectDemo {
    public static void main(String[] args) {
        final StringBuilder result1 = new StringBuilder();
        final StringBuilder result2 = new StringBuilder();

        PublishSubject<String> subject = PublishSubject.create();
        subject.subscribe(value -> result1.append(value));
        subject.onNext("a");
        subject.onNext("b");
        subject.onNext("c");
        subject.subscribe(value -> result2.append(value));
        subject.onNext("d");
        subject.onComplete();

        //Output will be abcd
        out.println(result1);
        //Output will be d only
        //as subscribed after c item emitted.
        out.println(result2);
    }
}
