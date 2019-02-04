package ru.goryachev.tochka.di

import android.content.Context
import com.google.gson.Gson
import com.vk.sdk.VKSdk
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import ru.goryachev.tochka.feature.github.data.GithubApi
import ru.goryachev.tochka.feature.login.data.SocialRepository
import ru.goryachev.tochka.feature.global.presentation.DefaultErrorHandler
import ru.goryachev.tochka.feature.global.presentation.ErrorHandler
import ru.goryachev.tochka.feature.login.domain.SocialInteractor
import ru.goryachev.tochka.model.AndroidResourceProvider
import ru.goryachev.tochka.model.ResourceProvider
import ru.goryachev.tochka.model.system.flow.FlowRouter
import ru.goryachev.tochka.model.system.flow.GlobalRouter
import ru.goryachev.tochka.model.system.prefs.Preferences
import ru.goryachev.tochka.model.system.rx.RxSchedulers
import ru.goryachev.tochka.model.system.rx.SchedulersProvider
import ru.terrakok.cicerone.Cicerone
import ru.terrakok.cicerone.NavigatorHolder
import toothpick.config.Module

fun module(func: (Module.() -> (Unit))) = object : Module() {
    init {
        func()
    }
}

fun moduleApp(context: Context): Module = module {
    bind(Context::class.java).toInstance(context.applicationContext)
    bind(Preferences::class.java).singletonInScope()
    bind(Gson::class.java).toInstance(Gson())
    bind(ResourceProvider::class.java).to(AndroidResourceProvider::class.java).singletonInScope()
    bind(ErrorHandler::class.java).to(DefaultErrorHandler::class.java).singletonInScope()

    val cicerone = Cicerone.create(GlobalRouter())
    bind(GlobalRouter::class.java).toInstance(cicerone.router)
    bind(NavigatorHolder::class.java).toInstance(cicerone.navigatorHolder)
    bind(SchedulersProvider::class.java).toInstance(RxSchedulers())
}

fun moduleNetwork(): Module = module {
    bind(OkHttpClient::class.java).toProvider(OkHttpProvider::class.java).providesSingletonInScope()
    bind(Retrofit::class.java).toProvider(RetrofitProvider::class.java).providesSingletonInScope()
    bind(GithubApi::class.java).toProvider(GithubApiProvider::class.java).providesSingletonInScope()
}

fun moduleSocial(context: Context): Module = module {
    bind(VKSdk::class.java).toInstance(VKSdk.initialize(context))
    bind(SocialRepository::class.java).singletonInScope()
    bind(SocialInteractor::class.java).singletonInScope()
}

fun flowModule(func: Module.() -> Unit) = module {
    bind(FlowRouter::class.java).toProvider(LocalRouterProvider::class.java).providesSingletonInScope()
    bind(Cicerone::class.java).toProvider(LocalCiceroneProvider::class.java).singletonInScope()
    bind(NavigatorHolder::class.java).toProvider(LocalNavigatorProvider::class.java).providesSingletonInScope()
    func.invoke(this)
}
