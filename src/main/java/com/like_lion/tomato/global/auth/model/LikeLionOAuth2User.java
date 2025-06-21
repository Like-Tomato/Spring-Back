package com.like_lion.tomato.global.auth.model;

import com.like_lion.tomato.global.auth.dto.UserInfo;
import com.like_lion.tomato.global.auth.model.provider.OAuth2ProviderUser;
import lombok.ToString;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.core.user.OAuth2User;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

@ToString
public class LikeLionOAuth2User implements OAuth2User {
    private Map<String, Object> attributes;
    private UserInfo userInfo;

    // 이미 존재하는 회원 생성자
    public LikeLionOAuth2User(UserInfo userInfo) {
        this.userInfo = userInfo;
    }
    // 새로 가입한 회원 생성자
    public LikeLionOAuth2User(UserInfo userInfo, OAuth2ProviderUser oAuth2ProviderUser) {
        this.userInfo = userInfo;
        this.attributes = oAuth2ProviderUser.getAttributes();
    }

    public static LikeLionOAuth2User from(UserInfo dto) {
        return new LikeLionOAuth2User(dto);
    }

    public static LikeLionOAuth2User of(UserInfo dto, OAuth2ProviderUser oAuth2ProviderUser) {
        return new LikeLionOAuth2User(dto, oAuth2ProviderUser);
    }

    @Override
    public Map<String, Object> getAttributes() {
        return this.attributes;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<GrantedAuthority> authorities = new ArrayList<>();
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(userInfo.getRole());
        authorities.add(simpleGrantedAuthority);
        return authorities;
    }

    @Override
    public String getName() {
        return this.userInfo.getUsername();
    }

    public Long getId() {
        return this.userInfo.getId();
    }

    public String getEmail() {
        return this.userInfo.getEmail();
    }

    public String getRole() {
        return this.userInfo.getRole();
    }

    public String getProfileImage() {
        return this.userInfo.getProfileImageUrl();
    }

    public String getProvider() {
        return this.userInfo.getProvider();
    }
}
