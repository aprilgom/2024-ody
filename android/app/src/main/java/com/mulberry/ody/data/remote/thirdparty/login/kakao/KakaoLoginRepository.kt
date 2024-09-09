package com.mulberry.ody.data.remote.thirdparty.login.kakao

import android.content.Context
import com.mulberry.ody.data.local.db.OdyDatastore
import com.mulberry.ody.data.remote.core.entity.login.mapper.toAuthToken
import com.mulberry.ody.data.remote.core.entity.login.request.LoginRequest
import com.mulberry.ody.data.remote.core.service.LoginService
import com.mulberry.ody.data.remote.thirdparty.login.entity.UserProfile
import com.mulberry.ody.domain.apiresult.ApiResult
import com.mulberry.ody.domain.apiresult.map
import com.mulberry.ody.domain.common.flatMap
import com.mulberry.ody.domain.model.AuthToken
import com.mulberry.ody.domain.repository.ody.FCMTokenRepository

class KakaoLoginRepository(
    private val loginService: LoginService,
    private val odyDatastore: OdyDatastore,
    private val kakaoOAuthLoginService: KakaoOAuthLoginService,
    private val fcmTokenRepository: FCMTokenRepository,
) {
    fun checkIfLogined(): Boolean {
        return kakaoOAuthLoginService.checkIfLogined()
    }

    suspend fun login(context: Context): ApiResult<AuthToken> {
        val loginRequest = kakaoOAuthLoginService.login(context).flatMap { buildLoginRequest(it) }

        if (loginRequest.isFailure) {
            val loginRequestException =
                loginRequest.exceptionOrNull() ?: Exception("LoginRequest exception이 null입니다")
            return ApiResult.Unexpected(loginRequestException)
        }

        val result =
            loginRequest.getOrNull() ?: return ApiResult.Unexpected(
                Exception("LoginRequest 가 null 입니다"),
            )

        return loginService.postLoginWithKakao(result).map {
            val token = it.toAuthToken()
            odyDatastore.setAuthToken(token)
            token
        }
    }

    private suspend fun buildLoginRequest(userProfile: UserProfile): Result<LoginRequest> =
        fcmTokenRepository.fetchFCMToken().map { deviceToken ->
            LoginRequest(
                deviceToken,
                userProfile.providerId,
                userProfile.nickname,
                userProfile.imageUrl,
            )
        }
}