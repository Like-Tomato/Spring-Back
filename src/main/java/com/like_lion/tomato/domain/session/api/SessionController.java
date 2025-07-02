package com.like_lion.tomato.domain.session.api;

import com.like_lion.tomato.domain.session.dto.response.SessionListWithStateRes;
import com.like_lion.tomato.domain.session.dto.response.SessionDetailRes;
import com.like_lion.tomato.domain.session.service.SessionService;
import com.like_lion.tomato.global.auth.implement.JwtTokenProvider;
import com.like_lion.tomato.global.auth.service.JwtService;
import com.like_lion.tomato.global.exception.response.ApiResponse;
import com.like_lion.tomato.global.util.HttpHeaderUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/session")
@RestController
@Tag(name = "Session", description = "세션(주차별 강의/과제) 관련 API")
public class SessionController {

    private final SessionService sessionService;
    private final JwtService jwtService;

    @Operation(
            summary = "세션 전체/파트별 목록 + 제출여부 조회",
            description = """
                    세션 자료(주차별 강의/과제) 목록을 파트, 주차별로 필터링해서 조회합니다.
                    기본 주차별 오름차순 입니다.
                    각 세션별로 현재 로그인한 멤버의 과제 제출여부(submitted)까지 함께 반환합니다.
                    
                    - part: BACKEND, FRONTEND, DESIGN, PM, AI 등 (optional)
                    - week: 주차(숫자, optional)
                    - submitted: true(제출), false(미제출)
                    """,
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(name = "part", description = "세션 파트 필터 (예: BACKEND, FRONTEND, DESIGN, PM, AI)", example = "BACKEND"),
                    @Parameter(name = "week", description = "주차 필터 (숫자)", example = "1")
            }

    )
    @GetMapping
    @PreAuthorize("hasRole('MEMBER')")
    public ApiResponse<SessionListWithStateRes> readAllByFilter(
            @RequestParam(required = false) String part,
            @RequestParam(required = false) Integer week,
            HttpServletRequest request
    ) {
        String accessToken = HttpHeaderUtil.getAccessToken(request, JwtTokenProvider.BEARER_PREFIX);
        String memberId = jwtService.extractMemberIdFromAccessToken(accessToken);
        return ApiResponse.success(sessionService.readAllSessionsWithSubmissionState(memberId, part, week));
    }

    @Operation(
            summary = "세션 상세 + 제출 내역 조회",
            description = "특정 세션의 상세 정보와 해당 멤버의 과제 제출 내역을 반환합니다.",
            security = @SecurityRequirement(name = "bearerAuth"),
            parameters = {
                    @Parameter(name = "sessionId", description = "세션 ID", required = true)
            }
    )
    @GetMapping("/{sessionId}")
    @PreAuthorize("hasRole('MEMBER')")
    public ApiResponse<SessionDetailRes> readSessionWithAssignment(
            @PathVariable String sessionId,
            HttpServletRequest request
    ) {
        String accessToken = HttpHeaderUtil.getAccessToken(request, JwtTokenProvider.BEARER_PREFIX);
        String memberId = jwtService.extractMemberIdFromAccessToken(accessToken);
        return ApiResponse.success(sessionService.getSessionWithAssignment(sessionId, memberId));
    }
}
