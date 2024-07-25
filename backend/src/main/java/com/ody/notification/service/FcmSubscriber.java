package com.ody.notification.service;

import com.google.firebase.messaging.FirebaseMessaging;
import com.google.firebase.messaging.TopicManagementResponse;
import com.ody.common.exception.OdyServerErrorException;
import com.ody.meeting.domain.Meeting;
import com.ody.member.domain.DeviceToken;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class FcmSubscriber {

    public void subscribeTopic(Meeting meeting, DeviceToken deviceToken) {
        try {
            String topicName = "/topics/" + meeting.getId().toString();
            TopicManagementResponse topicManagementResponse = FirebaseMessaging.getInstance()
                    .subscribeToTopic(List.of(deviceToken.getDeviceToken()), topicName);
            log.info("모임 구독에 성공했습니다 {}", topicManagementResponse);
        } catch (Exception exception) {
            log.error(exception.getMessage());
            throw new OdyServerErrorException("모임 구독에 실패했습니다");
        }
    }
}
