package ru.goryachev.tochka.feature.github.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import ru.goryachev.tochka.feature.github.domain.GithubInteractor
import ru.goryachev.tochka.feature.global.presentation.RxError
import ru.goryachev.tochka.feature.global.presentation.viewmodel.BaseViewModel
import ru.goryachev.tochka.model.system.flow.FlowRouter
import ru.goryachev.tochka.model.system.rx.SchedulersProvider
import ru.goryachev.tochka.model.system.rx.subscribe
import ru.goryachev.tochka.feature.github.domain.GithubUserEntity
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GithubViewModel(private val router: FlowRouter,
                      private val githubInteractor: GithubInteractor,
                      private val schedulersProvider: SchedulersProvider) : BaseViewModel(router) {

    companion object {
        const val OFFSET = 30
    }

    val searchResponseLd = MutableLiveData<List<GithubUserEntity>>()
    val pageResponseLd = MutableLiveData<List<GithubUserEntity>>()

    var currentSearchQuery = ""

    fun onSearchQuery(query: String, listSize: Int) {
        if (query.isEmpty()) return
        currentSearchQuery = query
        /* Здесь и в других местах нужно показать диалог,
         но я пока не понял как лучше вызывать события view,
         livedata для этого не оч подходит */
        githubInteractor.searchUser(query, OFFSET, listSize)
                .observeOn(schedulersProvider.main())
                .compose(lifecycle())
                .subscribe({ searchResponseLd.value = it }, RxError.doNothing())

    }

    fun getNewPage(listSize: Int) {
        githubInteractor.searchUser(currentSearchQuery, OFFSET, listSize)
                .compose(lifecycle())
                .observeOn(schedulersProvider.main())
                .subscribe({
                    pageResponseLd.value = it
                }, RxError.doNothing())
    }
}

class GithubViewModelFactory @Inject constructor(private val router: FlowRouter,
                                                 private val githubInteractor: GithubInteractor,
                                                 private val schedulersProvider: SchedulersProvider) : ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return GithubViewModel(router, githubInteractor, schedulersProvider) as T
    }
}