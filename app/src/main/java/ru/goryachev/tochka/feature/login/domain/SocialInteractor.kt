package ru.goryachev.tochka.feature.login.domain

import ru.goryachev.tochka.feature.login.data.SocialRepository
import javax.inject.Inject

class SocialInteractor @Inject constructor(private val socialRepository: SocialRepository) {

    fun getUserInfo() = socialRepository.getUserInfo()

    fun logout() = socialRepository.logout()
}