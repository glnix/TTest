package ru.goryachev.tochka.feature.global.presentation

import io.reactivex.functions.Consumer
import retrofit2.HttpException
import ru.goryachev.tochka.R
import ru.goryachev.tochka.feature.global.domain.CommonException
import ru.goryachev.tochka.feature.global.presentation.view.ErrorView
import ru.goryachev.tochka.model.ResourceProvider
import timber.log.Timber
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.inject.Inject


interface ErrorHandler {
    fun proceed(errorView: ErrorView? = null, action: ((CommonException) -> Unit)? = null): Consumer<Throwable>
    fun proceedInvoke(errorView: ErrorView? = null, e: Throwable, action: ((CommonException) -> Unit)? = null)
    fun commonExceptionToString(exception: CommonException): String
}

open class DefaultErrorHandler @Inject constructor(private val resourceProvider: ResourceProvider) : ErrorHandler {

    override fun proceedInvoke(errorView: ErrorView?, e: Throwable, action: ((CommonException) -> Unit)?) {
        proceed(errorView, action).accept(e)
    }

    override fun proceed(errorView: ErrorView?, action: ((CommonException) -> Unit)?): Consumer<Throwable> {
        return Consumer { e ->
            proceedException(errorView, e, action)
        }
    }

    private fun proceedException(errorView: ErrorView?, e: Throwable?, action: ((CommonException) -> Unit)?) {
        Timber.tag(javaClass.simpleName).w(e)
        when (e) {
            is HttpException -> proceedHttpException(errorView, e, action)
            is UnknownHostException,
            is SocketTimeoutException -> proceedNetworkException(errorView, action)
            is CommonException -> dispatchExceptionToListeners(e, errorView, action)
            else -> proceedUnknownException(errorView, action)
        }
    }

    protected open fun proceedHttpException(errorView: ErrorView?, e: HttpException, action: ((CommonException) -> Unit)?) {
        val message = e.message() ?: resourceProvider.getString(R.string.error_unknown)
        val exception = CommonException(message = message, code = CommonException.CODE_UNKNOWN)

        dispatchExceptionToListeners(exception, errorView, action)
    }

    private fun proceedNetworkException(errorView: ErrorView?, action: ((CommonException) -> Unit)?) {
        val exception = CommonException(CommonException.CODE_NETWORK, resourceProvider.getString(R.string.error_network))
        dispatchExceptionToListeners(exception, errorView, action)
    }

    protected fun proceedUnknownException(errorView: ErrorView?, action: ((CommonException) -> Unit)?) {
        val exception = CommonException(CommonException.CODE_UNKNOWN, resourceProvider.getString(R.string.error_unknown))
        dispatchExceptionToListeners(exception, errorView, action)
    }

    protected fun dispatchExceptionToListeners(exception: CommonException,
                                               errorView: ErrorView?,
                                               action: ((CommonException) -> Unit)?) {
        errorView?.showErrorMessage(commonExceptionToString(exception))
                ?: action?.invoke(exception)
    }

    override fun commonExceptionToString(exception: CommonException): String {
        val unknownMessage = resourceProvider.getString(R.string.error_unknown)
        return exception.message ?: unknownMessage
    }
}