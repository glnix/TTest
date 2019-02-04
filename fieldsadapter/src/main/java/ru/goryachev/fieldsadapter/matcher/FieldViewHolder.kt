package ru.goryachev.fieldsadapter.matcher

import android.os.Handler
import android.os.Looper
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView

open class FieldViewHolder<T>(itemView: View, protected var root: ViewGroup) : RecyclerView.ViewHolder(itemView) {

    protected var data: T? = null

    fun bind(data: T) {
        unbind()
        Handler(Looper.getMainLooper()).post { internalBind(data) }
        this.data = data
    }

    private fun internalBind(data: T) {
        performBind(data)
    }

    protected open fun performBind(data: T) {}

    protected open fun unbind() {}
}
