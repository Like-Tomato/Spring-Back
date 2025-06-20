package com.like_lion.tomato.domain.auth.api;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RequestMapping("/api/v1/auth")

@RestController
public class AuthController {

    private final InMemoryClientRegistrationRepository clientRegistrationRepository;

    public AuthController(InMemoryClientRegistrationRepository clientRegistrationRepository) {
        this.clientRegistrationRepository = clientRegistrationRepository;
    }

    /**
     * /login/google 요청시 앱 서버에서 직접 리디렉트 URI를 만들어 리디렉트시키는 방식
     * ResponseApi 객체가 생성되면 ResponseEntity 대신 ResponseApi로 수정할 예정!
     * (이후 아이디가 존재하면 바로 로그인 처리를, 존재하지 않으면 로그인 창으로 이동시킬 예정!)
     * @return
     */
    @GetMapping("/login/google")
    public ResponseEntity<Void> googleLogin() {
        ClientRegistration registration = clientRegistrationRepository.findByRegistrationId("google");
        String authUrl = UriComponentsBuilder.fromUriString(registration.getProviderDetails().getAuthorizationUri())
                .queryParam("client_id", registration.getClientId())
                .queryParam("redirect_uri", registration.getRedirectUri())
                .queryParam("response_type", "code")
                .queryParam("scope", String.join(" ", registration.getScopes()))
                // state는 보안상 랜덤값으로 생성 권장
                .queryParam("state", "random-generated-state")
                .build()
                .toUriString();
        return ResponseEntity.status(HttpStatus.SEE_OTHER)
                .location(URI.create(authUrl))
                .build();
    }


    @PostMapping("/logout")
    public ResponseEntity<Void> logout() {
        return null;
    }


    @GetMapping("/refresh")
    public ResponseEntity<Void> refresh() {
        return null;
    }

    @GetMapping("/me")
    public ResponseEntity<Void> me() {
        return null;
    }



}
