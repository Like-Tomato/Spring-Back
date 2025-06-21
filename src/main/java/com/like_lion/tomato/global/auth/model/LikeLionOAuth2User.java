package com.like_lion.tomato.global.auth.model;

import com.like_lion.tomato.global.auth.model.provider.OAuth2ProviderUser;
import com.nimbusds.openid.connect.sdk.claims.UserInfo;
import lombok.ToString;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.Map;

@ToString
public class LikeLionOAuth2User implements OAuth2User {
    private Map<String, Object> attributes;
    private UserInfo userInfo;

    public LikeLionOAuth2User(UserInfo userInfo, OAuth2ProviderUser oAuth2ProviderUser) {
        
    }
}
