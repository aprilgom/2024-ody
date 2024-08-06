package com.ody.meeting.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.assertAll;

import com.ody.common.BaseServiceTest;
import com.ody.common.Fixture;
import com.ody.common.exception.OdyNotFoundException;
import com.ody.mate.domain.Mate;
import com.ody.mate.domain.Nickname;
import com.ody.mate.dto.response.MateResponse;
import com.ody.mate.repository.MateRepository;
import com.ody.meeting.domain.Meeting;
import com.ody.meeting.dto.request.MeetingSaveRequestV1;
import com.ody.meeting.dto.response.MeetingFindByMemberResponse;
import com.ody.meeting.dto.response.MeetingSaveResponseV1;
import com.ody.meeting.dto.response.MeetingWithMatesResponse;
import com.ody.meeting.repository.MeetingRepository;
import com.ody.member.domain.DeviceToken;
import com.ody.member.domain.Member;
import com.ody.member.repository.MemberRepository;
import com.ody.util.InviteCodeGenerator;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

class MeetingServiceTest extends BaseServiceTest {

    @Autowired
    private MeetingService meetingService;

    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private MeetingRepository meetingRepository;

    @Autowired
    private MateRepository mateRepository;

    @DisplayName("내 약속 목록 조회 시 오름차순 정렬한다.")
    @Test
    void findAllByMember() {
        Member member = memberRepository.save(
                new Member(new DeviceToken("Bearer device-token=new-member-device-token")));

        Meeting meetingDayAfterTomorrowAt14 = meetingRepository.save(Fixture.ODY_MEETING4);
        Meeting meetingTomorrowAt12 = meetingRepository.save(Fixture.ODY_MEETING3);
        Meeting meetingTomorrowAt14 = meetingRepository.save(Fixture.ODY_MEETING5);

        mateRepository.save(
                new Mate(meetingDayAfterTomorrowAt14, member, new Nickname("제리1"), Fixture.ORIGIN_LOCATION));
        mateRepository.save(new Mate(meetingTomorrowAt12, member, new Nickname("제리2"), Fixture.ORIGIN_LOCATION));
        mateRepository.save(new Mate(meetingTomorrowAt14, member, new Nickname("제리3"), Fixture.ORIGIN_LOCATION));

        List<MeetingFindByMemberResponse> meetings = meetingService.findAllByMember(member).meetings();

        List<Long> meetingIds = meetings.stream()
                .map(MeetingFindByMemberResponse::id)
                .toList();

        assertThat(meetingIds).containsExactly(
                meetingTomorrowAt12.getId(),
                meetingTomorrowAt14.getId(),
                meetingDayAfterTomorrowAt14.getId()
        );
    }

    @DisplayName("약속 저장 및 초대 코드 갱신에 성공한다")
    @Test
    void saveV1Success() {
        Meeting testMeeting = Fixture.ODY_MEETING1;
        MeetingSaveRequestV1 request = new MeetingSaveRequestV1(
                testMeeting.getName(),
                testMeeting.getDate(),
                testMeeting.getTime(),
                testMeeting.getTarget().getAddress(),
                testMeeting.getTarget().getLatitude(),
                testMeeting.getTarget().getLongitude()
        );

        MeetingSaveResponseV1 response = meetingService.saveV1(request);

        assertAll(
                () -> assertThat(response.name()).isEqualTo(request.name()),
                () -> assertThat(response.date()).isEqualTo(request.date()),
                () -> assertThat(response.time()).isEqualTo(request.time()),
                () -> assertThat(response.targetAddress()).isEqualTo(request.targetAddress()),
                () -> assertThat(response.targetLatitude()).isEqualTo(request.targetLatitude()),
                () -> assertThat(response.targetLongitude()).isEqualTo(request.targetLongitude()),
                () -> assertThat(InviteCodeGenerator.decode(response.inviteCode())).isEqualTo(response.id())
        );
    }

    @DisplayName("약속과 참여자들 정보를 조회한다.")
    @Test
    void findMeetingWithMatesSuccess() {
        Member member1 = memberRepository.save(Fixture.MEMBER1);
        Member member2 = memberRepository.save(Fixture.MEMBER2);

        Meeting meeting = meetingRepository.save(Fixture.ODY_MEETING1);

        Mate mate1 = new Mate(meeting, member1, new Nickname("조조"), Fixture.ORIGIN_LOCATION);
        Mate mate2 = new Mate(meeting, member2, new Nickname("제리"), Fixture.ORIGIN_LOCATION);

        mateRepository.save(mate1);
        mateRepository.save(mate2);

        MeetingWithMatesResponse response = meetingService.findMeetingWithMates(member1, meeting.getId());
        List<String> mateNicknames = response.mates().stream()
                .map(MateResponse::nickname)
                .toList();

        assertAll(
                () -> assertThat(response.id()).isEqualTo(meeting.getId()),
                () -> assertThat(mateNicknames).containsOnly(mate1.getNicknameValue(), mate2.getNicknameValue())
        );
    }

    @DisplayName("약속 조회 시, 약속이 존재하지 않으면 예외가 발생한다.")
    @Test
    void findMeetingWithMatesException() {
        Member member = memberRepository.save(Fixture.MEMBER1);

        assertThatThrownBy(() -> meetingService.findMeetingWithMates(member, 1L))
                .isInstanceOf(OdyNotFoundException.class);
    }
}