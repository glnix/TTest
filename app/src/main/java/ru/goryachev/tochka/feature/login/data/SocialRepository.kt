package ru.goryachev.tochka.feature.login.data

import android.content.Context
import android.net.Uri
import com.facebook.AccessToken
import com.facebook.FacebookException
import com.facebook.Profile
import com.facebook.internal.Utility
import com.facebook.login.LoginManager
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.vk.sdk.VKSdk
import com.vk.sdk.api.VKApi
import com.vk.sdk.api.VKError
import com.vk.sdk.api.VKRequest
import com.vk.sdk.api.VKResponse
import com.vk.sdk.api.model.VKApiUser
import com.vk.sdk.api.model.VKList
import io.reactivex.Completable
import io.reactivex.Single
import io.reactivex.SingleEmitter
import org.json.JSONObject
import ru.goryachev.tochka.feature.login.domain.LoginType
import ru.goryachev.tochka.feature.login.domain.UserInfo
import ru.goryachev.tochka.model.system.prefs.Preferences
import javax.inject.Inject


class SocialRepository @Inject constructor(val context: Context, val preferences: Preferences) {

    companion object {
        private const val AVATAR_SIZE = 200
    }

    fun getUserInfo(): Single<UserInfo> =
            when (preferences.authState) {
                LoginType.VK.toString() -> getVkUserInfo()
                LoginType.FB.toString() -> getFbUserInfo()
                LoginType.GOOGLE.toString() -> getGoogleUserInfo()
                else -> throw IllegalArgumentException()
            }

    private fun getVkUserInfo(): Single<UserInfo> {
        return Single.create { emitter ->
            VKApi.users().get().executeWithListener(object : VKRequest.VKRequestListener() {
                override fun onComplete(response: VKResponse) {
                    val usersList = response.parsedModel as VKList<VKApiUser>
                    val currentUser = usersList.component1()
                    val photoUrl = currentUser.photo_200
                    val name = "${currentUser.first_name.orEmpty()} ${currentUser.last_name.orEmpty()}"
                    emitter.onSuccess(UserInfo(name, photoUrl, LoginType.VK))
                }

                override fun onError(error: VKError) {
                    emitter.onError(Exception(error.errorMessage))
                }
            })
        }
    }

    private fun getFbUserInfo(): Single<UserInfo> {
        return Single.create { emitter ->
            try {
                val profile = Profile.getCurrentProfile()
                if (profile != null)
                    dispatchProfile(emitter, profile)
                else fetchFbProfile { dispatchProfile(emitter, it) }
            } catch (e: Exception) {
                emitter.onError(Exception(e.message.orEmpty()))
            }
        }
    }

    private fun getGoogleUserInfo(): Single<UserInfo> {
        return Single.create { emitter ->
            val account = GoogleSignIn.getLastSignedInAccount(context)
            if (account != null) {
                val currentUser = UserInfo(account.displayName.orEmpty(), account.photoUrl?.toString().orEmpty(), LoginType.GOOGLE)
                emitter.onSuccess(currentUser)
            } else {
                emitter.onError(Exception("Account not found"))
            }
        }
    }

    private fun dispatchProfile(emitter: SingleEmitter<UserInfo>, profile: Profile) {
        emitter.onSuccess(UserInfo(profile.name, profile.getProfilePictureUri(AVATAR_SIZE, AVATAR_SIZE).toString(), LoginType.FB))
    }

    private fun fetchFbProfile(func: ((Profile) -> Unit)) {
        Utility.getGraphMeRequestWithCacheAsync(AccessToken.getCurrentAccessToken().token,
                object : Utility.GraphMeRequestWithCacheCallback {
                    override fun onSuccess(userInfo: JSONObject) {
                        val id = userInfo.optString("id") ?: return
                        val link = userInfo.optString("link")
                        val profile = Profile(
                                id,
                                userInfo.optString("first_name"),
                                userInfo.optString("middle_name"),
                                userInfo.optString("last_name"),
                                userInfo.optString("name"),
                                if (link != null) Uri.parse(link) else null
                        )
                        Profile.setCurrentProfile(profile)
                        func.invoke(profile)
                    }

                    override fun onFailure(error: FacebookException) {
                        return
                    }
                })
    }

    fun logout() = Completable.create {
        when (preferences.authState) {
            LoginType.VK.toString() -> VKSdk.logout()
            LoginType.FB.toString() -> LoginManager.getInstance().logOut()
            LoginType.GOOGLE.toString() -> googleLogout()
        }
        preferences.authState = LoginType.NON_AUTH.toString()

        it.onComplete()
    }

    private fun googleLogout() {
        val options = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestProfile()
                .build()
        GoogleSignIn.getClient(context, options).revokeAccess()
    }
}