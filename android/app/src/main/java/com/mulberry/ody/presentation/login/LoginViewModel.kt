package com.mulberry.ody.presentation.login

import android.content.Context
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.mulberry.ody.data.remote.thirdparty.login.kakao.KakaoLoginRepository
import com.mulberry.ody.domain.apiresult.onFailure
import com.mulberry.ody.domain.apiresult.onNetworkError
import com.mulberry.ody.domain.apiresult.onSuccess
import com.mulberry.ody.domain.repository.ody.AuthTokenRepository
import com.mulberry.ody.presentation.common.BaseViewModel
import com.mulberry.ody.presentation.common.MutableSingleLiveData
import com.mulberry.ody.presentation.common.SingleLiveData
import kotlinx.coroutines.launch

class LoginViewModel(
    private val authTokenRepository: AuthTokenRepository,
    private val kakaoLoginRepository: KakaoLoginRepository,
    private val savedStateHandle: SavedStateHandle,
) : BaseViewModel() {
    private val _navigatedReason: MutableSingleLiveData<LoginNavigatedReason> = MutableSingleLiveData()
    val navigatedReason: SingleLiveData<LoginNavigatedReason> get() = _navigatedReason

    private val _navigateAction: MutableSingleLiveData<LoginNavigateAction> =
        MutableSingleLiveData()
    val navigateAction: SingleLiveData<LoginNavigateAction> get() = _navigateAction

    fun checkIfNavigated() {
        savedStateHandle.get<LoginNavigatedReason>(NAVIGATED_REASON) ?.let { reason ->
            _navigatedReason.setValue(reason)
        }
    }

    fun checkIfLogined() {
        viewModelScope.launch {
            authTokenRepository.fetchAuthToken().onSuccess {
                if (kakaoLoginRepository.checkIfLogined() && it.accessToken.isNotEmpty()) {
                    navigateToMeetings()
                }
            }
        }
    }

    fun loginWithKakao(context: Context) {
        viewModelScope.launch {
            kakaoLoginRepository.login(context)
                .onSuccess {
                    navigateToMeetings()
                }.onFailure { code, errorMessage ->
                    handleError()
                }.onNetworkError {
                    handleNetworkError()
                    lastFailedAction = { loginWithKakao(context) }
                }
        }
    }

    private fun navigateToMeetings() {
        _navigateAction.setValue(LoginNavigateAction.NavigateToMeetings)
    }

    companion object {
        private val NAVIGATED_REASON = "NAVIGATED_REASON"
    }
}