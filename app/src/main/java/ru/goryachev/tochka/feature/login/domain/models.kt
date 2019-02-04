package ru.goryachev.tochka.feature.login.domain

data class UserInfo(val name: String, val photoUrl: String, val type: LoginType)

enum class LoginType {
    FB, VK, GOOGLE, NON_AUTH
}