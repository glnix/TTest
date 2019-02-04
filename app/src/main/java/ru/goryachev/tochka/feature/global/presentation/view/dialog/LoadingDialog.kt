package ru.goryachev.tochka.feature.global.presentation.view.dialog


import android.app.Dialog
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import ru.goryachev.tochka.R
import ru.goryachev.tochka.feature.global.presentation.view.LoadingView
import ru.goryachev.tochka.model.ui.extentions.showFragment

class LoadingDialog : DialogFragment() {
    init {
        setStyle(STYLE_NORMAL, R.style.AppTheme)
    }


    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        return AlertDialog.Builder(activity!!)
                .setView(R.layout.dialog_loading)
                .create()
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        isCancelable = false
    }


    companion object {

        fun view(fm: FragmentManager): LoadingView {
            return object : LoadingView {
                private val tag = LoadingDialog::class.java.name


                override fun showLoadingIndicator() {
                    fm.showFragment(LoadingDialog(), tag = tag)
                }

                override fun hideLoadingIndicator() {
                    val dialog = (fm.findFragmentByTag(tag) as LoadingDialog?)
                    dialog?.dismissAllowingStateLoss()
                }
            }
        }

        fun view(fragment: Fragment): LoadingView {
            return view(fragment.fragmentManager!!)
        }
    }
}