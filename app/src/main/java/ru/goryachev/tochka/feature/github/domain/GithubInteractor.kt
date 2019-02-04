package ru.goryachev.tochka.feature.github.domain

import io.reactivex.Single
import ru.goryachev.tochka.feature.github.data.GithubRepository
import javax.inject.Inject

class GithubInteractor @Inject constructor(private val githubRepository: GithubRepository) {

    fun searchUser(query: String, count: Int, offset: Int): Single<List<GithubUserEntity>> {
        val page = if (offset < count) 0 else Math.ceil(offset.toDouble() / count.toDouble()).toInt()
        return githubRepository.search(query, count, page)
    }
}