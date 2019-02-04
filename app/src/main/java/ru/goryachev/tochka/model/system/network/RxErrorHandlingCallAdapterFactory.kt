package ru.goryachev.tochka.model.system.network

import io.reactivex.*
import retrofit2.Call
import retrofit2.CallAdapter
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import java.lang.reflect.Type

class RxErrorHandlingCallAdapterFactory private constructor(scheduler: Scheduler) : CallAdapter.Factory() {

    companion object {
        fun create(scheduler: Scheduler): RxErrorHandlingCallAdapterFactory {
            return RxErrorHandlingCallAdapterFactory(scheduler)
        }
    }

    private val original = RxJava2CallAdapterFactory.createWithScheduler(scheduler)

    override fun get(returnType: Type?, annotations: Array<out Annotation>?, retrofit: Retrofit?): CallAdapter<*, *> {
        return RxCallAdapterWrapper(original.get(returnType, annotations, retrofit) as CallAdapter<*, *>)
    }


    private class RxCallAdapterWrapper<T>(private val wrappedAdapter: CallAdapter<T, *>) : CallAdapter<T, Any> {
        override fun responseType(): Type = wrappedAdapter.responseType()

        override fun adapt(call: Call<T>?): Any {
            val delegate = wrappedAdapter.adapt(call)
            return when (delegate) {
                is Flowable<*> -> wrapFlowable(delegate)
                is Observable<*> -> wrapObservable(delegate)
                is Single<*> -> wrapSingle(delegate)
                is Maybe<*> -> wrapMaybe(delegate)
                else -> return delegate
            }
        }

        private fun wrapFlowable(delegate: Flowable<*>): Any {
            return delegate.map(this::handleFailResponse)
        }

        private fun wrapObservable(delegate: Observable<*>): Any {
            return delegate.map(this::handleFailResponse)
        }

        private fun wrapSingle(delegate: Single<*>): Any {
            return delegate.map(this::handleFailResponse)
        }

        private fun wrapMaybe(delegate: Maybe<*>): Any {
            return delegate.map(this::handleFailResponse)
        }

        private fun handleFailResponse(response: Any): Any {
            return response
        }
    }
}