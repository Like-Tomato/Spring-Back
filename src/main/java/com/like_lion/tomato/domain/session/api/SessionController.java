package com.like_lion.tomato.domain.session.api;

import com.like_lion.tomato.domain.session.dto.SessionListRes;
import com.like_lion.tomato.domain.session.dto.response.SessionDetailRes;
import com.like_lion.tomato.domain.session.service.SessionService;
import com.like_lion.tomato.global.auth.implement.JwtTokenProvider;
import com.like_lion.tomato.global.auth.service.JwtService;
import com.like_lion.tomato.global.exception.response.ApiResponse;
import com.like_lion.tomato.global.util.HttpHeaderUtil;
import com.like_lion.tomato.infra.s3.dto.FileRegisterReq;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/session")
@RestController
public class SessionController {

    private final SessionService sessionService;
    private final JwtService jwtService;

    /**
     * 세션 전체/파트별 목록 조회
     * @param part (optional) 파트명 (예: BACKEND, FRONTEND, DESIGN)
     * @return 세션 목록 (최신순)
     */
    @GetMapping
    @PreAuthorize("hasRole('MEMBER')")
    public ApiResponse<SessionListRes> readAllByFilter(@RequestParam(required = false) String part) {
        return ApiResponse.success(sessionService.readAllSessions(part));
    }

    /**
     * 세션 상세/제출파일 상세정보 조회
     * @param sessionId 세션 ID (PathVariable)
     * @param request HttpServletRequest (헤더에서 토큰 추출용)
     * @return 세션 상세 정보 + 해당 멤버의 과제 제출 내역 등
     */
    @GetMapping("/{sessionId}")
    @PreAuthorize("hasRole('MEMBER')")
    public ApiResponse<SessionDetailRes> readSessionWithAssignment(
            @PathVariable String sessionId,
            HttpServletRequest request
    ) {
        // 1. 헤더에서 Bearer 토큰 추출
        String accessToken = HttpHeaderUtil.getAccessToken(request, JwtTokenProvider.BEARER_PREFIX);

        // 2. JwtService로부터 멤버 정보 추출
        String memberId = jwtService.extractMemberIdFromAccessToken(accessToken);

        // 3. 서비스 호출 (세션 + 파일 + 과제 + 제출 내역 등)
        return ApiResponse.success(sessionService.getSessionWithAssignment(sessionId, memberId));
    }

    /**
     * 세션 자료(파일) 등록 (ADMIN만 가능)
     * @param sessionId 세션 ID
     * @param req 파일 등록 요청 DTO(fileKey, name, mimeType, size)
     * @param request HttpServletRequest (accessToken에서 memberId 추출)
     */
    @PostMapping("/{sessionId}/file")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<?> registerSessionFile(
            @PathVariable String sessionId,
            @RequestBody @Valid FileRegisterReq req,
            HttpServletRequest request
    ) {
        String accessToken = HttpHeaderUtil.getAccessToken(request, JwtTokenProvider.BEARER_PREFIX);
        String memberId = jwtService.extractMemberIdFromAccessToken(accessToken);

        sessionService.registerFile(sessionId, memberId, req);
        return ApiResponse.success("세션 파일 등록 성공");
    }
}
