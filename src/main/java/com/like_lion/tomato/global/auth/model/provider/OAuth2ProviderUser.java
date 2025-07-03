package com.like_lion.tomato.global.auth.model.provider;

import com.like_lion.tomato.domain.member.entity.Member;
import com.like_lion.tomato.global.auth.exception.AuthErrorCode;
import com.like_lion.tomato.global.auth.exception.AuthException;
import com.like_lion.tomato.global.auth.model.Role;
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
                return new GoogleUser(attributes, registrationId); // googleUser 객체로 반환
            default:
                // apiResponse가 만들어지면 다음으로 수정
                // throw new BadRequestExceptoin(ErrorCode.BAD_REQUEST, "일치하는 제공자가 없습니다");
                throw new AuthException(AuthErrorCode.PROVIDER_NOT_FOUND);
        }
    }

    public Member toMember(Role role, String fileKey) {
        return Member.builder()
                .username(this.getUsername())
                .email(this.getEmail())
                .provider(this.getProvider())
                .fileKey(fileKey)
                .role(role)
                .build();
    }

    public abstract String getEmail();
    public abstract String getUsername();
    public abstract String getProviderId();
    public abstract String getProfileUrl();
    public String getProvider() {
        return provider;
    }
    public Map<String, Object> getAttributes() {
        return attributes;
    }

}
