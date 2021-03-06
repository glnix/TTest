package ru.goryachev.tochka.model.ui.pagination

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import java.util.*

internal abstract class LMHelper<T : RecyclerView.LayoutManager> private constructor(protected val layoutManager: T) {

    abstract val firstVisibleItemPosition: Int
    abstract val lastVisibleItemPosition: Int

    private class LinearLMHelper internal constructor(layoutManager: LinearLayoutManager) : LMHelper<LinearLayoutManager>(layoutManager) {

        override val firstVisibleItemPosition: Int
            get() = layoutManager.findFirstVisibleItemPosition()

        override val lastVisibleItemPosition: Int
            get() = layoutManager.findLastVisibleItemPosition()
    }

    private class StaggeredGridLMHelper internal constructor(layoutManager: StaggeredGridLayoutManager) : LMHelper<StaggeredGridLayoutManager>(layoutManager) {

        override val firstVisibleItemPosition: Int
            get() = Collections.min(asList(layoutManager.findFirstVisibleItemPositions(null)))

        override val lastVisibleItemPosition: Int
            get() {
                val into = layoutManager.findLastVisibleItemPositions(null)
                return Collections.max(asList(into))
            }

        private fun asList(ints: IntArray): List<Int> = ints.toList()
    }

    companion object {

        fun <Type : RecyclerView.LayoutManager> getInstance(layoutManager: Type?): LMHelper<*> {
            return when (layoutManager) {
                is LinearLayoutManager -> LinearLMHelper(layoutManager)
                is StaggeredGridLayoutManager -> StaggeredGridLMHelper(layoutManager)
                else -> throw RuntimeException("Unknown LayoutManager class: " + layoutManager?.javaClass)
            }
        }
    }
}