package ru.goryachev.tochka.feature.login.presentation.vk

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import ru.goryachev.tochka.di.DI
import ru.goryachev.tochka.di.moduleFlow
import ru.goryachev.tochka.feature.global.FlowFragment
import ru.goryachev.tochka.feature.login.Fb.GoogleLoginViewModel
import ru.goryachev.tochka.feature.login.Fb.GoogleLoginViewModelFactory
import ru.goryachev.tochka.model.system.flow.FlowNavigator
import ru.goryachev.tochka.model.system.flow.GlobalRouter
import toothpick.Toothpick

class GoogleLoginFragment : FlowFragment() {

    companion object {
        private const val REQUEST_CODE_GOOGLE_AUTH = 2503

        fun newInstance(): GoogleLoginFragment {
            return GoogleLoginFragment().apply {
                arguments = Bundle()
            }
        }
    }

    val viewModelFactory = Toothpick.openScopes(DI.SCOPE_APP, scopeName).moduleFlow {
        bind(GoogleLoginViewModelFactory::class.java)
    }.getInstance(GoogleLoginViewModelFactory::class.java).also {
        Toothpick.closeScope(scopeName)
    }

    lateinit var model: GoogleLoginViewModel
    private val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestProfile().build()
    private val client by lazy { GoogleSignIn.getClient(activity!!, options) }


    override fun provideNavigator(router: GlobalRouter): FlowNavigator = object : FlowNavigator(this, router) {
        override fun createFragment(screenKey: String?, data: Any?): Fragment? = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model = ViewModelProviders.of(this, viewModelFactory).get(GoogleLoginViewModel::class.java)

        startLogin()
    }

    private fun startLogin() {
        val currentUser = GoogleSignIn.getLastSignedInAccount(activity)
        if (currentUser != null)
        else {
            startActivityForResult(client.signInIntent, REQUEST_CODE_GOOGLE_AUTH)
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (requestCode == REQUEST_CODE_GOOGLE_AUTH && resultCode == Activity.RESULT_OK) {
            val currentUser = GoogleSignIn.getSignedInAccountFromIntent(data)
            model.onResult()
        } else
            model.onError()
            super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onBackPressed() {
        model.onBackPressed()
    }

}