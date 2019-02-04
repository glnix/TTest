package ru.goryachev.tochka.feature.profile.presentation

import android.annotation.SuppressLint
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.goryachev.tochka.Screens
import ru.goryachev.tochka.feature.global.presentation.RxError
import ru.goryachev.tochka.feature.global.presentation.viewmodel.BaseViewModel
import ru.goryachev.tochka.feature.login.domain.SocialInteractor
import ru.goryachev.tochka.model.system.flow.FlowRouter
import ru.goryachev.tochka.model.system.prefs.Preferences
import ru.goryachev.tochka.model.system.rx.SchedulersProvider
import ru.goryachev.tochka.model.system.rx.subscribe
import javax.inject.Inject
import javax.inject.Singleton


@Singleton
class ProfileViewModel(val router: FlowRouter,
                       val prefs: Preferences,
                       val socialInteractor: SocialInteractor,
                       val schedulersProvider: SchedulersProvider) : BaseViewModel(router) {

    val nameLd = MutableLiveData<String>()
    val photoLd = MutableLiveData<String>()

    @SuppressLint("CheckResult")
    fun onViewCreated() {
        socialInteractor.getUserInfo()
                .observeOn(schedulersProvider.main())
                .compose(lifecycle())
                .subscribe({
                    nameLd.value = it.name
                    photoLd.value = it.photoUrl
                }, RxError.doNothing())
    }

    fun logout() {
        socialInteractor.logout()
                .subscribe({ router.startNewRootFlow(Screens.FLOW_LOGIN)}, RxError.doNothing())
    }
}

class ProfileViewModelFactory @Inject constructor(private val router: FlowRouter,
                                                  private val prefs: Preferences,
                                                  private val schedulersProvider: SchedulersProvider,
                                                  private val socialInteractor: SocialInteractor) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return ProfileViewModel(router, prefs, socialInteractor, schedulersProvider) as T
    }
}