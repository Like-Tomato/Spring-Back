package com.like_lion.tomato.domain.session.api;

import com.like_lion.tomato.domain.session.dto.SessionListRes;
import com.like_lion.tomato.domain.session.service.SessionService;
import com.like_lion.tomato.global.exception.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/session")
@RestController
public class SessionController {

    private final SessionService sessionService;

    @GetMapping
    @PreAuthorize("hasRole('MEMBER')")
    public ApiResponse<SessionListRes> readAllByFilter(@RequestParam(required = false) String part) {
        return ApiResponse.success(sessionService.readAllSessions(part));
    }
}