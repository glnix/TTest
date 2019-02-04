package ru.goryachev.tochka.model.system.rx

import android.annotation.SuppressLint
import io.reactivex.*
import io.reactivex.functions.Action
import io.reactivex.functions.Consumer
import ru.goryachev.tochka.feature.global.presentation.RxDecor
import ru.goryachev.tochka.feature.global.presentation.view.LoadingView

fun <T> Observable<T>.loading(loadingView: LoadingView): Observable<T> = compose(RxDecor.loading(loadingView))

fun <T> Flowable<T>.loading(loadingView: LoadingView): Flowable<T> = compose(RxDecor.loading(loadingView))

fun <T> Single<T>.loading(loadingView: LoadingView): Single<T> = compose(RxDecor.loading(loadingView))

fun <T> Maybe<T>.loading(loadingView: LoadingView): Maybe<T> = compose(RxDecor.loading(loadingView))

fun Completable.loading(loadingView: LoadingView): Completable = compose(RxDecor.loading<Any>(loadingView))


@SuppressLint("CheckResult")
fun <T> Observable<T>.subscribe(func: (T) -> Unit, consumer: Consumer<Throwable>) {
    subscribe(Consumer<T> { func(it) }, consumer)
}

@SuppressLint("CheckResult")
fun <T> Flowable<T>.subscribe(func: (T) -> Unit, consumer: Consumer<Throwable>) {
    subscribe(Consumer<T> { func(it) }, consumer)
}

@SuppressLint("CheckResult")
fun <T> Single<T>.subscribe(func: (T) -> Unit, consumer: Consumer<Throwable>) {
    subscribe(Consumer<T> { func(it) }, consumer)
}

@SuppressLint("CheckResult")
fun <T> Maybe<T>.subscribe(func: (T) -> Unit, consumer: Consumer<Throwable>) {
    subscribe(Consumer<T> { func(it) }, consumer)
}

@SuppressLint("CheckResult")
fun Completable.subscribe(func: () -> Unit, consumer: Consumer<Throwable>) {
    subscribe(Action { func() }, consumer)
}