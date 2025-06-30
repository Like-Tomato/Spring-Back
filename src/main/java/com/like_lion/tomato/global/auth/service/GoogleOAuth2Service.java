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
     * /login/google 요청시 앱 서버에서 직접 리디렉트 URI를 만들어 리디렉트시키는 방식
     * ResponseApi 객체가 생성되면 ResponseEntity 대신 ResponseApi로 수정할 예정!
     * (이후 아이디가 존재하면 바로 로그인 처리를, 존재하지 않으면 로그인 창으로 이동시킬 예정!)
     * @return
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
