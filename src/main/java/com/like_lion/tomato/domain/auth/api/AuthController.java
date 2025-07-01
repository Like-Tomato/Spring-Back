package com.like_lion.tomato.domain.auth.api;

import com.like_lion.tomato.global.auth.dto.TockenDto;
import com.like_lion.tomato.global.auth.implement.JwtTockenProvider;
import com.like_lion.tomato.global.auth.service.GoogleOAuth2Service;
import com.like_lion.tomato.global.auth.service.JwtService;
import com.like_lion.tomato.global.exception.response.ApiResponse;
import com.like_lion.tomato.global.util.CookieUtil;
import com.like_lion.tomato.global.util.HttpHeaderUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")

@RestController
public class AuthController {

    private final GoogleOAuth2Service googleOAuth2Service;
    private final JwtService jwtService;
    private final JwtTockenProvider jwtTockenProvider;


    @GetMapping("/login/google")
    public ApiResponse<ApiResponse.MessageDataWithData<String>> googleLogin() {
        String authUri = googleOAuth2Service.generateGoogleAuthUrl();
        return ApiResponse.success(
                "구글 로그인 URL이 생성되었습니다.",
                authUri
                );
    }


    @PostMapping("/logout")
    public ApiResponse<ApiResponse.MessageData> logout(
            HttpServletRequest request,
            HttpServletResponse response
    )
    {
        String refreshTocken = CookieUtil.getRefreshTocken(request);
        jwtService.logout(refreshTocken);
        CookieUtil.deleteRefreshCookie(response);
        return ApiResponse.success("로그아웃이 완료되었습니다.");
    }


    @GetMapping("/refresh")
    public ApiResponse<ApiResponse.MessageData> refresh(
            HttpServletRequest request,
            HttpServletResponse response
    )
    {
        String refreshTocken = CookieUtil.getRefreshTocken(request);
        TockenDto tockenDto = jwtService.tockenRefresh(refreshTocken);

        // 토큰 리프레시 jwtService에서 모듈화 로직!
        HttpHeaderUtil.setAccessTocken(response, tockenDto.getAccessTocken());
        int refreshMaxAge = (int) (jwtTockenProvider.getRefreshTockenExpiration() / 1000);
        CookieUtil.setRefreshCookies(response, tockenDto.getRefreshTocken(), refreshMaxAge);

        return ApiResponse.success("토큰 리프레시 성공");
    }
}
