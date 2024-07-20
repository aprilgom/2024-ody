package com.ody.notification.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.verify;

import com.ody.notification.domain.NotificationType;
import com.ody.notification.dto.request.FcmSendRequest;
import java.time.LocalDateTime;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.boot.test.mock.mockito.MockBean;

@SpringBootTest(webEnvironment = WebEnvironment.NONE)
class FcmEventSchedulerTest {

    @MockBean
    private FcmPushSender fcmPushSender;

    @DisplayName("예약 알림이 2초 후에 전송된다")
    @Test
    void testScheduledNotificationIsSentAtCorrectTime() throws InterruptedException {
        LocalDateTime sendAt = LocalDateTime.now().plusSeconds(2);
        FcmSendRequest fcmSendRequest = new FcmSendRequest(
                "testToken",
                NotificationType.DEPARTURE_REMINDER,
                sendAt
        );

        // 비동기 작업을 동기화 시키기 위한 클래스
        // 파라미터 인자에 비동기 작업의 개수를 입력해준다.
        // 입력된 개수의 비동기 작업이 종료될 때 까지 스레드는 대기 한다.
        CountDownLatch latch = new CountDownLatch(1);

        // Mokito의 doAnswer()는 특정 메서드가 호출될 때 수행할 작업을 정의한다.
        // fcmPushSender의 sendPushNotification 메서드가 호출될 때, latch.countDown()을 호출하여 카운트를 감소시킨다.
        // latch가 현재 1로 설정되어 있기 때문에 카운트가 감소되어 0개가 되면 대기하고 있던 스레드가 계속 작업을 진행할 수 있게 된다.
        doAnswer(invocation -> {
            latch.countDown();
            return null;
        }).when(fcmPushSender).sendPushNotification(fcmSendRequest);

        // 2초후에 메세지가 가도록 설정한 fcmSendRequest를 인자로 넣어 실제 sendPushNotification()를 호출한다.
        fcmPushSender.sendPushNotification(fcmSendRequest);

        // latch의 카운트가 0이될 때까지 대기할 시간을 정의한다.
        assertThat(latch.await(2, TimeUnit.SECONDS)).isTrue();

        // sendPushNotification() 메서드가 호출되었는지 검증한다.
        verify(fcmPushSender).sendPushNotification(fcmSendRequest);
    }
}