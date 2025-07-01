package com.like_lion.tomato.domain.session.api;

import com.like_lion.tomato.domain.session.dto.SessionListRes;
import com.like_lion.tomato.domain.session.dto.response.SessionDetailRes;
import com.like_lion.tomato.domain.session.service.SessionService;
import com.like_lion.tomato.global.auth.implement.JwtTokenProvider;
import com.like_lion.tomato.global.auth.service.JwtService;
import com.like_lion.tomato.global.exception.response.ApiResponse;
import com.like_lion.tomato.global.util.HttpHeaderUtil;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/session")
@RestController
public class SessionController {

    private final SessionService sessionService;
    private final JwtService jwtService;

    @GetMapping
    @PreAuthorize("hasRole('MEMBER')")
    public ApiResponse<SessionListRes> readAllByFilter(@RequestParam(required = false) String part) {
        return ApiResponse.success(sessionService.readAllSessions(part));
    }

    @GetMapping("/{sessionId}")
    public ApiResponse<SessionDetailRes> readSessionWithAssignment(
            @PathVariable String sessionId,
            HttpServletRequest request // HttpServletRequest로 헤더 접근
    ) {
        // 1. 헤더에서 Bearer 토큰 추출
        String accessToken = HttpHeaderUtil.getAccessToken(request, JwtTokenProvider.BEARER_PREFIX);

        // 2. JwtService로부터 멤버 정보 추출
        String memberId = jwtService.extractMemberIdFromAccessToken(accessToken);

        // 3. 서비스 호출
        return ApiResponse.success(sessionService.getSessionWithAssignment(sessionId, memberId));
    }
}