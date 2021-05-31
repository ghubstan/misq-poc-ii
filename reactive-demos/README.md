# RxJava 3.X

https://github.com/ReactiveX/RxJava

## Wiki

https://github.com/ReactiveX/RxJava/wiki

## Tutorials

https://www.tutorialspoint.com/rxjava/index.htm

## Observable

Observable provides data once subscriber starts listening.

Observable can emit any number of items.

Observable can emit only signal of completion as well with no item.

Observable can terminate successfully.

Observable may never terminate. e.g. a button can be clicked any number of times.

Observable may throw error at any point of time.

## Subscriber

Observable can have multiple subscribers.

When an Observable emits an item, each subscriber `onNext()` method gets invoked.

When an Observable finished emitting items, each subscriber `onComplete()` method gets invoked.

If an Observable emits error, each subscriber `onError()` method gets invoked.

## ReactiveX Operators

http://reactivex.io/documentation/operators.html

### ReactiveX Operator Categories

http://reactivex.io/documentation/operators.html#categorized

### ReactiveX Operator Decision Tree

http://reactivex.io/documentation/operators.html#tree

### ReactiveX Operators - Full List

http://reactivex.io/documentation/operators.html#alphabetical
