package ru.goryachev.tochka.feature.login.presentation.vk

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.facebook.CallbackManager
import com.facebook.FacebookCallback
import com.facebook.FacebookException
import com.facebook.login.LoginManager
import com.facebook.login.LoginResult
import ru.goryachev.tochka.di.DI
import ru.goryachev.tochka.di.moduleFlow
import ru.goryachev.tochka.feature.global.FlowFragment
import ru.goryachev.tochka.feature.login.presentation.fb.FbLoginViewModel
import ru.goryachev.tochka.feature.login.presentation.fb.FbLoginViewModelFactory
import ru.goryachev.tochka.model.system.flow.FlowNavigator
import ru.goryachev.tochka.model.system.flow.GlobalRouter
import toothpick.Toothpick

class FbLoginFragment : FlowFragment(), FacebookCallback<LoginResult> {

    companion object {

        val PERMISSIONS = arrayListOf("public_profile")

        fun newInstance(): GoogleLoginFragment {
            return GoogleLoginFragment().apply {
                arguments = Bundle()
            }
        }
    }

    val viewModelFactory = Toothpick.openScopes(DI.SCOPE_APP, scopeName).moduleFlow {
        bind(FbLoginViewModelFactory::class.java)
    }.getInstance(FbLoginViewModelFactory::class.java).also {
        Toothpick.closeScope(scopeName)
    }

    lateinit var model: FbLoginViewModel
    private val callbackManager = CallbackManager.Factory.create()


    override fun provideNavigator(router: GlobalRouter): FlowNavigator = object : FlowNavigator(this, router) {
        override fun createFragment(screenKey: String?, data: Any?): Fragment? = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model = ViewModelProviders.of(this, viewModelFactory).get(FbLoginViewModel::class.java)

        startLogin()
    }

    private fun startLogin() {
        LoginManager.getInstance().registerCallback(callbackManager, this)
        LoginManager.getInstance().logInWithReadPermissions(this, PERMISSIONS)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        callbackManager.onActivityResult(requestCode, resultCode, data)
        super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onError(error: FacebookException?) {
        model.onError()
    }

    override fun onSuccess(result: LoginResult?) {
        model.onResult()
    }

    override fun onCancel() {
        model.onError()
    }

    override fun onBackPressed() {
        model.onBackPressed()
    }

    override fun onDetach() {
        LoginManager.getInstance().unregisterCallback(callbackManager)
        super.onDetach()
    }
}