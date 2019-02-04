package ru.goryachev.tochka.feature.github.data

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query

interface GithubApi {

    @GET("search/users")
    fun search(@Query("q") query: String,
               @Query("per_page") pageSize: Int,
               @Query("page") page: Int): Single<GithubSearchResponse>
}