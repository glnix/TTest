package ru.goryachev.tochka.feature.github.presentation

import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.jakewharton.rxbinding2.widget.RxSearchView
import io.reactivex.disposables.Disposable
import io.reactivex.functions.Consumer
import kotlinx.android.synthetic.main.fmt_github_search.*
import kotlinx.android.synthetic.main.toolbar.*
import ru.goryachev.tochka.R
import ru.goryachev.tochka.di.DI
import ru.goryachev.tochka.di.moduleFlow
import ru.goryachev.tochka.feature.global.FlowFragment
import ru.goryachev.tochka.feature.global.presentation.RxError
import ru.goryachev.tochka.model.system.flow.FlowNavigator
import ru.goryachev.tochka.model.system.flow.GlobalRouter
import ru.goryachev.tochka.model.system.rx.subscribe
import ru.goryachev.tochka.model.ui.pagination.paginationObservable
import ru.goryachev.tochka.feature.github.domain.GithubUserEntity
import toothpick.Toothpick
import java.util.concurrent.TimeUnit

class GithubFragment : FlowFragment() {

    companion object {
        const val DEBOUNCE_TIME = 600L

        fun newInstance(): GithubFragment {
            return GithubFragment().apply {
                arguments = Bundle()
            }
        }
    }

    private val usersAdapter = GithubUsersAdapter()
    private var paginationDisposable: Disposable? = null

    override val layoutRes = R.layout.fmt_github_search

    val viewModelFactory = Toothpick.openScopes(DI.SCOPE_APP, scopeName).moduleFlow {
        bind(GithubViewModelFactory::class.java)
    }.getInstance(GithubViewModelFactory::class.java).also {
        Toothpick.closeScope(scopeName)
    }

    lateinit var model: GithubViewModel

    override fun provideNavigator(router: GlobalRouter): FlowNavigator = object : FlowNavigator(this, router) {
        override fun createFragment(screenKey: String?, data: Any?): Fragment? = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        model = ViewModelProviders.of(this, viewModelFactory).get(GithubViewModel::class.java)
        initUI()
        initLiveData()
    }

    private fun initUI() {
        setUpToolbar()
        RxSearchView.queryTextChanges(searchView)
                .map(CharSequence::toString)
                .debounce(DEBOUNCE_TIME, TimeUnit.MILLISECONDS)
                .subscribe({ model.onSearchQuery(it, usersAdapter.itemCount) }, RxError.doNothing())

        val lmanager = LinearLayoutManager(activity, RecyclerView.VERTICAL, false)

        with(rv) {
            layoutManager = lmanager
            adapter = usersAdapter
        }
    }

    private fun initLiveData() {
        model.searchResponseLd.observe(this, Observer(this::updateList))
        model.pageResponseLd.observe(this, Observer(this::addPage))
    }

    private fun updateList(usersList: List<GithubUserEntity>) {
        usersAdapter.changeDataSet(usersList)
        rv.scrollToPosition(0)
        updatePagination()
    }

    private fun addPage(page: List<GithubUserEntity>) {
        usersAdapter.addDataSet(page)
        updatePagination()
    }

    private fun providePaginationDisposable(): Disposable {
        return rv.paginationObservable(GithubViewModel.OFFSET).subscribe(Consumer { model.getNewPage(usersAdapter.itemCount) }, RxError.doNothing())
    }

    private fun updatePagination() {
        if (paginationDisposable?.isDisposed == false)
            paginationDisposable?.dispose()
        paginationDisposable = providePaginationDisposable()
    }

}