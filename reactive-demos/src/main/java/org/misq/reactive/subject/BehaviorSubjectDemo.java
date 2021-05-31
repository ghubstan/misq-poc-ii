package org.misq.reactive.subject;

import io.reactivex.subjects.BehaviorSubject;

import static java.lang.System.out;

/**
 * BehaviorSubject emits the most recent item it has observed and then all
 * subsequent observed items to each subscribed Observer.
 */
public class BehaviorSubjectDemo {
    public static void main(String[] args) {
        final StringBuilder result1 = new StringBuilder();
        final StringBuilder result2 = new StringBuilder();
        BehaviorSubject<String> subject = BehaviorSubject.create();
        subject.subscribe(value -> result1.append(value));
        subject.onNext("a");
        subject.onNext("b");
        subject.onNext("c");
        subject.subscribe(value -> result2.append(value));
        subject.onNext("d");
        subject.onComplete();
        //Output will be abcd
        out.println(result1);
        //Output will be cd being BehaviorSubject
        //(c is last item emitted before subscribe)
        out.println(result2);
    }
}
