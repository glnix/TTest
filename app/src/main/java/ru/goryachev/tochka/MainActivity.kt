package ru.goryachev.tochka

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import ru.goryachev.tochka.di.DI
import ru.goryachev.tochka.di.inject
import ru.goryachev.tochka.feature.global.FlowFragment
import ru.goryachev.tochka.model.system.flow.FragmentNavigator
import ru.goryachev.tochka.model.system.flow.GlobalRouter
import ru.terrakok.cicerone.NavigatorHolder
import toothpick.Toothpick
import javax.inject.Inject

class MainActivity : AppCompatActivity() {

    @Inject
    lateinit var navigatorHolder: NavigatorHolder

    @Inject
    lateinit var router: GlobalRouter

    private val navigator by lazy { provideNavigator() }

    private fun provideNavigator(): FragmentNavigator {
        return object : FragmentNavigator(applicationContext, supportFragmentManager, R.id.flowContainer) {
            override fun createFragment(screenKey: String, data: Any?): Fragment? = Screens.getFlowFragment(screenKey, data)

            override fun exit() {
                finish()
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        Toothpick.openScope(DI.SCOPE_APP).inject(this)
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (savedInstanceState == null)
            navigator.setLaunchScreen(Screens.FLOW_SPLASH)


    }

    override fun onResumeFragments() {
        super.onResumeFragments()
        navigatorHolder.setNavigator(navigator)
    }

    override fun onPause() {
        navigatorHolder.removeNavigator()
        super.onPause()
    }

    override fun onBackPressed() {
        val fragment = supportFragmentManager.findFragmentById(R.id.flowContainer)
        (fragment as? FlowFragment)?.onBackPressed()
    }
}