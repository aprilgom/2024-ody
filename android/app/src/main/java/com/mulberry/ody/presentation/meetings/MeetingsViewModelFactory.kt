package com.mulberry.ody.presentation.meetings

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.mulberry.ody.domain.repository.ody.MeetingRepository
import com.mulberry.ody.presentation.common.analytics.AnalyticsHelper

class MeetingsViewModelFactory(
    private val analyticsHelper: AnalyticsHelper,
    private val meetingRepository: MeetingRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return if (modelClass.isAssignableFrom(MeetingsViewModel::class.java)) {
            MeetingsViewModel(
                analyticsHelper,
                meetingRepository,
            ) as T
        } else {
            throw IllegalArgumentException("Unknown ViewModel class")
        }
    }
}
