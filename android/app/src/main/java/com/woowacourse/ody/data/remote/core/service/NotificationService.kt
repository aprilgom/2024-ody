package com.woowacourse.ody.data.remote.core.service

import com.woowacourse.ody.data.remote.core.entity.notification.response.NotificationLogsResponse
import com.woowacourse.ody.domain.apiresult.ApiResult
import retrofit2.http.GET
import retrofit2.http.Path

interface NotificationService {
    @GET(NOTIFICATION_LOG_PATH)
    suspend fun fetchNotificationLogs(
        @Path("meetingId") meetingId: Long,
    ): ApiResult<NotificationLogsResponse>

    companion object {
        const val NOTIFICATION_LOG_PATH = "/meetings/{meetingId}/noti-log"
    }
}