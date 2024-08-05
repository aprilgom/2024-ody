package com.woowacourse.ody.fake

import com.woowacourse.ody.domain.model.NotificationLog
import com.woowacourse.ody.domain.model.NotificationType
import com.woowacourse.ody.domain.repository.ody.NotificationLogRepository
import java.time.LocalDateTime

object FakeNotificationLogRepository : NotificationLogRepository {
    val entryALog =
        NotificationLog(
            NotificationType.ENTRY,
            "A",
            LocalDateTime.of(2024, 7, 7, 14, 30),
        )
    val entryBLog =
        NotificationLog(
            NotificationType.ENTRY,
            "B",
            LocalDateTime.of(2024, 7, 7, 14, 31),
        )
    val entryCLog =
        NotificationLog(
            NotificationType.ENTRY,
            "C",
            LocalDateTime.of(2024, 7, 7, 14, 32),
        )
    val departureReminderALog =
        NotificationLog(
            NotificationType.DEPARTURE_REMINDER,
            "A",
            LocalDateTime.of(2024, 7, 7, 14, 33),
        )
    val departureReminderBLog =
        NotificationLog(
            NotificationType.DEPARTURE_REMINDER,
            "B",
            LocalDateTime.of(2024, 7, 7, 14, 34),
        )
    val departureReminderCLog =
        NotificationLog(
            NotificationType.DEPARTURE_REMINDER,
            "C",
            LocalDateTime.of(2024, 7, 7, 14, 35),
        )
    val departureALog =
        NotificationLog(
            NotificationType.DEPARTURE,
            "A",
            LocalDateTime.of(2024, 7, 7, 14, 36),
        )
    val departureBLog =
        NotificationLog(
            NotificationType.DEPARTURE,
            "B",
            LocalDateTime.of(2024, 7, 7, 14, 37),
        )
    val departureCLog =
        NotificationLog(
            NotificationType.DEPARTURE,
            "C",
            LocalDateTime.of(2024, 7, 7, 14, 38),
        )

    override suspend fun fetchNotificationLogs(meetingId: Long): Result<List<NotificationLog>> =
        Result.success(
            listOf(
                entryALog,
                entryBLog,
                entryCLog,
                departureReminderALog,
                departureReminderBLog,
                departureReminderCLog,
                departureALog,
                departureBLog,
                departureCLog,
                departureCLog,
                departureCLog,
                departureCLog,
                departureCLog,
                departureCLog,
                departureCLog,
                departureCLog,
                departureCLog,
                departureCLog,
                departureCLog,
                departureCLog,
                departureCLog,
                departureCLog,
                departureCLog,
                departureCLog,
                departureCLog,
                departureCLog,
                departureCLog,
                departureCLog,
                departureCLog,
                departureCLog,
                departureCLog,
                departureCLog,
                departureCLog,
                departureCLog,
                departureCLog,
                departureCLog,
            ),
        )
}