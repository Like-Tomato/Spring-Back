package com.like_lion.tomato.global.auth.service;

import com.like_lion.tomato.domain.auth.exception.AuthErrorCode;
import com.like_lion.tomato.domain.auth.exception.AuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.ClientRegistrationRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.Optional;
import java.util.UUID;

import static com.fasterxml.jackson.databind.jsonFormatVisitors.JsonValueFormat.UUID;

@RequiredArgsConstructor
@Service
public class GoogleOAuth2Service {

    private final ClientRegistrationRepository clientRegistrationRepository;

    /**
     * 구글 OAuth2 인증 페이지 URL을 생성하여 반환합니다.
     * <p>
     * 사용자가 구글 로그인을 요청할 때, 서버에서 직접 생성한 state 값을 포함한
     * 구글 인증 페이지 URL을 만들어 클라이언트에 제공합니다.
     * </p>
     *
     * @return 생성된 구글 인증 페이지 URL 문자열
     * @throws AuthException 구글 ClientRegistration이 등록되어 있지 않은 경우 발생
     */
    public String generateGoogleAuthUrl() {

        ClientRegistration clientRegistration = clientRegistrationRepository.findByRegistrationId("google");
        if (clientRegistration == null) {
            throw new AuthException(AuthErrorCode.GOOGLE_CLIENT_REGISTRATION_NOT_FOUND);
        }
        String state = java.util.UUID.randomUUID().toString();
        return UriComponentsBuilder.fromUriString(clientRegistration.getProviderDetails().getAuthorizationUri())
                .queryParam("client_id", clientRegistration.getClientId())
                .queryParam("ridirect_uri", clientRegistration.getRedirectUri())
                .queryParam("response_type", "code")
                .queryParam("scope", String.join(" "), clientRegistration.getScopes())
                .queryParam("state", state)
                .build()
                .toUriString();
    }
}
