package ru.goryachev.tochka.di

import com.google.gson.Gson
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.goryachev.tochka.BuildConfig
import ru.goryachev.tochka.feature.github.data.GithubApi
import ru.goryachev.tochka.model.system.flow.FlowRouter
import ru.goryachev.tochka.model.system.flow.GlobalRouter
import ru.goryachev.tochka.model.system.network.CurlLoggingInterceptor
import ru.goryachev.tochka.model.system.network.RxErrorHandlingCallAdapterFactory
import ru.goryachev.tochka.model.system.rx.SchedulersProvider
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Provider


class LocalRouterProvider @Inject constructor(private val router: GlobalRouter) : Provider<FlowRouter> {
    override fun get(): FlowRouter = FlowRouter(router)
}

class LocalCiceroneProvider @Inject constructor(private val router: FlowRouter) : Provider<Cicerone<FlowRouter>> {
    override fun get(): Cicerone<FlowRouter> = Cicerone.create(router)
}

class LocalNavigatorProvider @Inject constructor(private val cicerone: Cicerone<FlowRouter>) : Provider<NavigatorHolder> {
    override fun get(): NavigatorHolder = cicerone.navigatorHolder
}

class OkHttpProvider @Inject constructor() : Provider<OkHttpClient> {

    companion object {
        private const val TIMEOUT_CONNECTION = 30L
        private const val TIMEOUT_READ = 30L
        private const val TIMEOUT_WRITE = 30L
    }

    override fun get(): OkHttpClient {
        return OkHttpClient.Builder()
                .connectTimeout(TIMEOUT_CONNECTION, TimeUnit.SECONDS)
                .readTimeout(TIMEOUT_READ, TimeUnit.SECONDS)
                .writeTimeout(TIMEOUT_WRITE, TimeUnit.SECONDS)
                .addInterceptor(CurlLoggingInterceptor())
                .build()
    }
}

class RetrofitProvider @Inject constructor(private val okHttpClient: OkHttpClient,
                                           private val gson: Gson,
                                           private val schedulersProvider: SchedulersProvider) : Provider<Retrofit> {
    override fun get(): Retrofit {
        return Retrofit.Builder()
                .baseUrl(BuildConfig.ENDPOINT)
                .client(okHttpClient)
                .addCallAdapterFactory(RxErrorHandlingCallAdapterFactory.create(schedulersProvider.io()))
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
    }
}

class GithubApiProvider @Inject constructor(private val retrofit: Retrofit) : Provider<GithubApi> {
    override fun get(): GithubApi {
        return retrofit.create(GithubApi::class.java)
    }
}