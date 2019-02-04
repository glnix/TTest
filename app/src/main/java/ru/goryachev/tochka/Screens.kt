package ru.goryachev.tochka

import ru.goryachev.tochka.feature.global.FlowFragment
import ru.goryachev.tochka.feature.login.presentation.LoginFragment
import ru.goryachev.tochka.feature.login.presentation.vk.FbLoginFragment
import ru.goryachev.tochka.feature.login.presentation.vk.GoogleLoginFragment
import ru.goryachev.tochka.feature.login.presentation.vk.VkLoginFragment
import ru.goryachev.tochka.feature.main.presentation.MainScreenFragment
import ru.goryachev.tochka.feature.splash.presentation.view.SplashFragment

object Screens {

    const val FLOW_SPLASH = "FLOW_SPLASH"

    const val FLOW_LOGIN = "FLOW_LOGIN"
    const val FLOW_VK_LOGIN = "FLOW_VK_LOGIN"
    const val FLOW_FB_LOGIN = "FLOW_FB_LOGIN"
    const val FLOW_GOOGLE_LOGIN = "FLOW_GOOGLE_LOGIN"

    const val FLOW_MAIN = "FLOW_MAIN"


    @Suppress("UNCHECKED_CAST")
    fun getFlowFragment(flowKey: String, data: Any? = null): FlowFragment? {
        return when (flowKey) {
            FLOW_SPLASH -> SplashFragment.newInstance()
            FLOW_LOGIN -> LoginFragment.newInstance()
            FLOW_VK_LOGIN -> VkLoginFragment.newInstance()
            FLOW_FB_LOGIN -> FbLoginFragment.newInstance()
            FLOW_GOOGLE_LOGIN -> GoogleLoginFragment.newInstance()
            FLOW_MAIN -> MainScreenFragment.newInstance()
            else -> null
        }
    }
}