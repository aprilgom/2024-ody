package com.ody.mate.controller;

import com.ody.mate.dto.request.MateSaveRequestV2;
import com.ody.mate.dto.request.NudgeRequest;
import com.ody.mate.dto.response.MateSaveResponseV2;
import com.ody.member.domain.Member;
import com.ody.swagger.annotation.ErrorCode400;
import com.ody.swagger.annotation.ErrorCode401;
import com.ody.swagger.annotation.ErrorCode404;
import com.ody.swagger.annotation.ErrorCode500;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;

@Tag(name = "Mate API")
@SecurityRequirement(name = "Authorization")
public interface MateControllerSwagger {

    @Operation(
            summary = "약속 참여",
            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = MateSaveRequestV2.class))),
            responses = {
                    @ApiResponse(
                            responseCode = "201",
                            description = "약속 참여 성공",
                            content = @Content(schema = @Schema(implementation = MateSaveResponseV2.class))
                    )
            }
    )
    @ErrorCode400
    @ErrorCode401
    @ErrorCode404(description = "유효하지 않은 초대코드")
    @ErrorCode500
    ResponseEntity<MateSaveResponseV2> saveV2(
            @Parameter(hidden = true) Member member,
            MateSaveRequestV2 mateSaveRequest
    );

    @Operation(
            summary = "참여자 재촉하기",
            requestBody = @RequestBody(content = @Content(schema = @Schema(implementation = NudgeRequest.class))),
            responses = {@ApiResponse(responseCode = "200", description = "재촉하기 성공")}
    )
    @ErrorCode400(description = "유효하지 않은 mateId")
    @ErrorCode401
    @ErrorCode500
    ResponseEntity<Void> nudgeMate(NudgeRequest nudgeRequest);

    @Operation(
            summary = "참여자 약속 방 나가기",
            responses = {
                    @ApiResponse(
                            responseCode = "204",
                            description = "참여자 약속 방 나가기 성공"
                    )
            }
    )
    @ErrorCode401
    @ErrorCode404(description = "존재하지 않는 약속이거나 해당 약속 참여자가 아닌 경우")
    @ErrorCode500
    ResponseEntity<Void> leave(@Parameter(hidden = true) Member member, Long meetingId);
}
