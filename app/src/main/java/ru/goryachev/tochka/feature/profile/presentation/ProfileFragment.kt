package ru.goryachev.tochka.feature.profile.presentation

import android.os.Bundle
import android.view.View
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.fmt_drawer.*
import kotlinx.android.synthetic.main.header.view.*
import ru.goryachev.tochka.R
import ru.goryachev.tochka.di.DI
import ru.goryachev.tochka.di.moduleFlow
import ru.goryachev.tochka.feature.global.FlowFragment
import ru.goryachev.tochka.model.system.flow.FlowNavigator
import ru.goryachev.tochka.model.system.flow.GlobalRouter
import toothpick.Toothpick

class ProfileFragment : FlowFragment() {

    companion object {
        fun newInstance(): ProfileFragment {
            return ProfileFragment().apply {
                arguments = Bundle()
            }
        }
    }

    override val layoutRes = R.layout.fmt_drawer

    val viewModelFactory = Toothpick.openScopes(DI.SCOPE_APP, scopeName).moduleFlow {
        bind(ProfileViewModelFactory::class.java)
    }.getInstance(ProfileViewModelFactory::class.java).also {
        Toothpick.closeScope(scopeName)
    }

    lateinit var model: ProfileViewModel

    override fun provideNavigator(router: GlobalRouter): FlowNavigator = object : FlowNavigator(this, router) {
        override fun createFragment(screenKey: String?, data: Any?): Fragment? = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        model = ViewModelProviders.of(this, viewModelFactory).get(ProfileViewModel::class.java)
        initLiveData()
        initUI()
        model.onViewCreated()
    }

    private fun initUI() {
        navigationView.getHeaderView(0).logout.setOnClickListener { model.logout() }
    }

    private fun initLiveData() {
        //name
        val nameView = navigationView.getHeaderView(0).name
        model.nameLd.observe(this, Observer<String> { nameView.text = it })

        //photo
        model.photoLd.observe(this, Observer<String> { loadUserPhoto(it) })
    }

    private fun loadUserPhoto(url: String) {
        val photoView = navigationView.getHeaderView(0).photo
        Picasso.get().load(url).into(photoView)
    }
}