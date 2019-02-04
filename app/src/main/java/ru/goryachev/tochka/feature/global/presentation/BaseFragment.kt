package ru.goryachev.tochka.feature.global.presentation

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import ru.goryachev.tochka.R
import ru.goryachev.tochka.feature.global.presentation.view.dialog.LoadingDialog
import timber.log.Timber


abstract class BaseFragment : Fragment() {

    companion object {
        private fun hideKeyboard(activity: Activity) {
            val inputManager = activity
                    .getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager

            // check if no view has focus:
            val currentFocusedView = activity.currentFocus
            if (currentFocusedView != null) {
                inputManager.hideSoftInputFromWindow(currentFocusedView.windowToken, InputMethodManager.HIDE_NOT_ALWAYS)
            }
        }
    }

    protected val scopeName: String = this.javaClass.name
    protected abstract val layoutRes: Int
    protected open val statusBarColorRes: Int = R.color.colorAccentLight
    protected open val title: Int = R.string.empty
    protected val loadingView by lazy { LoadingDialog.view(this) }
    protected val errorView by lazy { RxError.view(childFragmentManager) { errorCloseCallback() } }

    protected val compatActivity: AppCompatActivity?
        get() = activity as? AppCompatActivity

    protected val actionBar: ActionBar?
        get() = compatActivity?.supportActionBar

    protected val toolbar: Toolbar?
        get() = view?.findViewById(R.id.toolbar)

    @SuppressLint("MissingSuperCall")
    final override fun onCreate(savedInstanceState: Bundle?) {
        try {
            injectDependencies()
        } catch (e: Exception) {
            Timber.d(e)
        } finally {
            super.onCreate(savedInstanceState)
        }
    }

    protected open fun injectDependencies() {}

    final override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return onSaveCreateView(inflater, container, savedInstanceState)
    }

    protected open fun onSaveCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(layoutRes, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setUpToolbar()
        setUpStatusBar()
    }

    protected open fun setUpToolbar() {
        if (title != 0) setTitle(title)
        activity?.invalidateOptionsMenu()
        compatActivity?.setSupportActionBar(toolbar)
    }

    protected open fun setUpStatusBar() {
        activity?.window?.statusBarColor = ContextCompat.getColor(activity!!, statusBarColorRes)
    }

    protected fun setEnableToolbarBackButton(enable: Boolean, action: (() -> Unit)? = { onBackPressed() }) {
        actionBar?.setHomeButtonEnabled(enable)
        actionBar?.setDisplayHomeAsUpEnabled(enable)
        toolbar?.setNavigationOnClickListener { action?.invoke() }
    }

    protected fun invalidateOptionsMenu() = compatActivity?.invalidateOptionsMenu()

    protected fun setTitle(res: Int) {
        activity?.setTitle(res)
    }

    protected fun setTitle(title: String?) {
        activity?.title = title
    }

    protected fun setSubtitle(subtitle: String?) {
        toolbar?.subtitle = subtitle
    }

    protected open fun onHideKeyboard() {
        activity?.let { hideKeyboard(it) }
    }

    abstract fun onBackPressed()

    protected open fun errorCloseCallback() {}
}