package ru.goryachev.tochka.feature.main.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import ru.goryachev.tochka.R
import ru.goryachev.tochka.di.DI
import ru.goryachev.tochka.di.moduleFlow
import ru.goryachev.tochka.feature.global.FlowFragment
import ru.goryachev.tochka.model.system.flow.FlowNavigator
import ru.goryachev.tochka.model.system.flow.GlobalRouter
import toothpick.Toothpick

class MainScreenFragment : FlowFragment() {

    companion object {
        fun newInstance(): MainScreenFragment {
            return MainScreenFragment().apply {
                arguments = Bundle()
            }
        }
    }

    override val layoutRes = R.layout.fmt_main

    val viewModelFactory = Toothpick.openScopes(DI.SCOPE_APP, scopeName).moduleFlow {
        bind(MainScreenViewModelFactory::class.java)
    }.getInstance(MainScreenViewModelFactory::class.java).also {
        Toothpick.closeScope(scopeName)
    }

    lateinit var model: MainScreenViewModel

    override fun provideNavigator(router: GlobalRouter): FlowNavigator = object : FlowNavigator(this, router) {
        override fun createFragment(screenKey: String?, data: Any?): Fragment? = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model = ViewModelProviders.of(this, viewModelFactory).get(MainScreenViewModel::class.java)
    }

    override fun onBackPressed() {
        model.onBackPressed()
    }
}