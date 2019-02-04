package ru.goryachev.tochka.feature.github.data

import java.util.ArrayList

data class GithubUserResponse(
        val id: String,
        val login: String,
        val avatar_url: String?)

data class GithubSearchResponse(val items: ArrayList<GithubUserResponse>)