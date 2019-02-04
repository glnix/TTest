package ru.goryachev.tochka.feature.login.presentation.vk

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.vk.sdk.VKAccessToken
import com.vk.sdk.VKCallback
import com.vk.sdk.VKSdk
import com.vk.sdk.VKServiceActivity
import com.vk.sdk.api.VKError
import ru.goryachev.tochka.di.DI
import ru.goryachev.tochka.di.moduleFlow
import ru.goryachev.tochka.feature.global.FlowFragment
import ru.goryachev.tochka.model.system.flow.FlowNavigator
import ru.goryachev.tochka.model.system.flow.GlobalRouter
import toothpick.Toothpick

class VkLoginFragment : FlowFragment(), VKCallback<VKAccessToken> {

    companion object {
        fun newInstance(): VkLoginFragment {
            return VkLoginFragment().apply {
                arguments = Bundle()
            }
        }

        private const val KEY_TYPE = "arg1"
        private const val KEY_SDK_CUSTOM_INITIALIZE = "arg4"
        private const val KEY_SCOPE_LIST = "arg2"

    }

    val viewModelFactory = Toothpick.openScopes(DI.SCOPE_APP, scopeName).moduleFlow {
        bind(VkLoginViewModelFactory::class.java)
    }.getInstance(VkLoginViewModelFactory::class.java).also {
        Toothpick.closeScope(scopeName)
    }

    lateinit var model: VkLoginViewModel

    override fun provideNavigator(router: GlobalRouter): FlowNavigator = object : FlowNavigator(this, router) {
        override fun createFragment(screenKey: String?, data: Any?): Fragment? = null
    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model = ViewModelProviders.of(this, viewModelFactory).get(VkLoginViewModel::class.java)

        startLogin()
    }

    private fun startLogin() {
        val intent = createIntent(this.context!!, VKServiceActivity.VKServiceType.Authorization)
        intent.putStringArrayListExtra(KEY_SCOPE_LIST, arrayListOf())
        startActivityForResult(intent, VKServiceActivity.VKServiceType.Authorization.outerCode)
    }

    private fun createIntent(appCtx: Context, type: VKServiceActivity.VKServiceType): Intent {
        val intent = Intent(appCtx, VKServiceActivity::class.java)
        intent.putExtra(KEY_TYPE, type.name)
        intent.putExtra(KEY_SDK_CUSTOM_INITIALIZE, VKSdk.isCustomInitialize())
        return intent
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        if (VKSdk.onActivityResult(requestCode, resultCode, data, this).not())
            super.onActivityResult(requestCode, resultCode, data)
    }

    override fun onResult(res: VKAccessToken?) {
        model.onResult()
    }

    override fun onError(error: VKError?) {
        model.onError()
    }

    override fun onBackPressed() {
        model.onBackPressed()
    }

}