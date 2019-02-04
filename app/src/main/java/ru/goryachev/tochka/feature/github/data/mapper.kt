package ru.goryachev.tochka.feature.github.data

import ru.goryachev.tochka.feature.github.domain.GithubUserEntity

fun GithubUserResponse.toEntity(): GithubUserEntity {
    return GithubUserEntity(id,
                            login,
                            avatar_url.orEmpty())
}