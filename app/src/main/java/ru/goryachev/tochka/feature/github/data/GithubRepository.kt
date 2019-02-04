package ru.goryachev.tochka.feature.github.data

import io.reactivex.Single
import ru.goryachev.tochka.model.system.rx.SchedulersProvider
import ru.goryachev.tochka.feature.github.domain.GithubUserEntity
import javax.inject.Inject

class GithubRepository @Inject constructor(private val api: GithubApi,
                                           private val schedulersProvider: SchedulersProvider) {

    fun search(query: String, count: Int, page: Int): Single<List<GithubUserEntity>> {
        return api.search(query, count, page)
                .map { it.items }
                .map { user -> user.map { it.toEntity() } }
                .observeOn(schedulersProvider.computation())
    }

}