package ru.goryachev.tochka.feature.github.presentation

import android.view.View
import android.view.ViewGroup
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.item_user.view.*
import ru.goryachev.fieldsadapter.matcher.FieldViewHolder
import ru.goryachev.tochka.R
import ru.goryachev.tochka.feature.global.presentation.view.adapter.LoadingAdapter
import ru.goryachev.tochka.feature.github.domain.GithubUserEntity

class GithubUsersAdapter : LoadingAdapter<GithubUserEntity>() {

    init {
        addXmlLayouter(R.layout.item_user) {
            matcher { it is GithubUserEntity }
            creator { view, viewGroup -> UserViewHolder(view, viewGroup) }
        }
    }


    private inner class UserViewHolder(itemView: View, root: ViewGroup) : FieldViewHolder<GithubUserEntity>(itemView, root) {

        override fun performBind(data: GithubUserEntity) {
            itemView.name.text = data.login

            Picasso.get()
                    .load(data.photoUrl)
                    .into(itemView.photo)
        }
    }
}