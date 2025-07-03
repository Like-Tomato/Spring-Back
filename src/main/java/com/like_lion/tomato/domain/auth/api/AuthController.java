package com.like_lion.tomato.domain.auth.api;

import com.like_lion.tomato.global.auth.dto.TokenDto;
import com.like_lion.tomato.global.auth.implement.JwtTokenProvider;
import com.like_lion.tomato.global.auth.service.GoogleOAuth2Service;
import com.like_lion.tomato.global.auth.service.JwtService;
import com.like_lion.tomato.global.exception.response.ApiResponse;
import com.like_lion.tomato.global.util.CookieUtil;
import com.like_lion.tomato.global.util.HttpHeaderUtil;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
@RestController
public class AuthController {

    private final GoogleOAuth2Service googleOAuth2Service;
    private final JwtService jwtService;
    private final JwtTokenProvider jwtTokenProvider;


    @GetMapping("/login/google")
    public ApiResponse<ApiResponse.MessageDataWithData<String>> googleLogin() {
        String authUri = googleOAuth2Service.generateGoogleAuthUrl();

        log.info("Google auth url: {}", authUri);

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
        String refreshToken = CookieUtil.getRefreshToken(request);
        jwtService.logout(refreshToken);
        CookieUtil.deleteRefreshCookie(response);
        return ApiResponse.success("로그아웃이 완료되었습니다.");
    }


    @GetMapping("/refresh")
    public ApiResponse<ApiResponse.MessageData> refresh(
            HttpServletRequest request,
            HttpServletResponse response
    )
    {
        String refreshToken = CookieUtil.getRefreshToken(request);
        TokenDto tokenDto = jwtService.tokenRefresh(refreshToken);

        // 토큰 리프레시 jwtService에서 모듈화 로직!
        HttpHeaderUtil.setAccessToken(response, tokenDto.getAccessToken());
        int refreshMaxAge = (int) (jwtTokenProvider.getRefreshTokenExpiration() / 1000);
        CookieUtil.setRefreshCookies(response, tokenDto.getRefreshToken(), refreshMaxAge);

        return ApiResponse.success("토큰 리프레시 성공");
    }
}
