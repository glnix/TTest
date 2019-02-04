package ru.goryachev.tochka.feature.login.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import kotlinx.android.synthetic.main.fmt_login.*
import ru.goryachev.tochka.R
import ru.goryachev.tochka.di.DI
import ru.goryachev.tochka.di.moduleFlow
import ru.goryachev.tochka.feature.global.FlowFragment
import ru.goryachev.tochka.feature.splash.presentation.viewmodel.SplashViewModelFactory
import ru.goryachev.tochka.model.system.flow.FlowNavigator
import ru.goryachev.tochka.model.system.flow.GlobalRouter
import toothpick.Toothpick

class LoginFragment : FlowFragment() {

    companion object {
        fun newInstance(): LoginFragment {
            return LoginFragment().apply {
                arguments = Bundle()
            }
        }
    }

    override val layoutRes: Int = R.layout.fmt_login

    val viewModelFactory = Toothpick.openScopes(DI.SCOPE_APP, scopeName).moduleFlow {
        bind(SplashViewModelFactory::class.java)
    }.getInstance(LoginViewModelFactory::class.java).also {
        Toothpick.closeScope(scopeName)
    }

    lateinit var model: LoginViewModel

    override fun provideNavigator(router: GlobalRouter): FlowNavigator = object : FlowNavigator(this, router) {
        override fun createFragment(screenKey: String?, data: Any?): Fragment? = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model = ViewModelProviders.of(this, viewModelFactory).get(LoginViewModel::class.java)

        initUI()

    }

    private fun initUI() {
        btnVk.setOnClickListener { model.loginVk() }
        btnFb.setOnClickListener { model.loginFb() }
        btnGoogle.setOnClickListener { model.loginGoogle() }
    }

    override fun onBackPressed() {
        model.onBackPressed()
    }
}