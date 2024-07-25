package com.woowacourse.ody.presentation.splash

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.woowacourse.ody.domain.model.Meeting
import com.woowacourse.ody.domain.repository.MeetingRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel(
    private val meetingRepository: MeetingRepository,
) : ViewModel() {
    private val _meeting: MutableLiveData<Meeting?> = MutableLiveData()
    val meeting: LiveData<Meeting?> get() = _meeting

    init {
        fetchMeeting()
    }

    private fun fetchMeeting() =
        viewModelScope.launch {
            delay(1500)
            meetingRepository.fetchMeeting().onSuccess {
                _meeting.postValue(it.first())
            }.onFailure {
                _meeting.postValue(null)
            }
        }
}
