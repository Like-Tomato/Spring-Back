package com.like_lion.tomato.global.auth.model.provider;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

public abstract class OAuth2ProviderUser {

    private String provider;
    private final Map<String, Object> attributes;

    protected OAuth2ProviderUser(Map<String, Object> attributes, String provider) {
        this.attributes = attributes;
        this.provider = provider;
    }

    // 팩토리 메서드
    public static OAuth2ProviderUser create(Map<String, Object> attributes, String registrationId) {
        switch (registrationId) {
            case "google":
                return null; // googleUser 객체로 반환
            default:
                // apiResponse가 만들어지면 다음으로 수정
                // throw new BadRequestExceptoin(ErrorCode.BAD_REQUEST, "일치하는 제공자가 업습ㄴ디ㅏ.");
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "일치하는 제공자가 없습니다.");
        }
    }

    // toMember 메서드 생성 예정!

    public abstract String getEmail();
    public abstract String getUsername();
    public abstract String getProviderId();
    public abstract String getProfileImageUrl();
    public String getProvider() {
        return provider;
    }
    public Map<String, Object> getAttributes() {
        return attributes;
    }

}
