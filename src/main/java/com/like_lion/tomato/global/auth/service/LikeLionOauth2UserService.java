package com.like_lion.tomato.global.auth.service;

import com.like_lion.tomato.domain.member.entity.Member;
import com.like_lion.tomato.domain.member.implement.MemberWriter;
import com.like_lion.tomato.domain.member.repository.MemberRepository;
import com.like_lion.tomato.global.auth.dto.UserInfo;
import com.like_lion.tomato.global.auth.model.LikeLionOAuth2User;
import com.like_lion.tomato.global.auth.model.provider.OAuth2ProviderUser;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.client.registration.ClientRegistration;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;
@Slf4j
@RequiredArgsConstructor
@Service
public class LikeLionOauth2UserService implements OAuth2UserService {


    private final MemberRepository memberRepository;
    private final MemberWriter memberWriter;

    @Override
    public OAuth2User loadUser(OAuth2UserRequest userRequest) throws OAuth2AuthenticationException {
        ClientRegistration clientRegistration = userRequest.getClientRegistration();
        OAuth2UserService<OAuth2UserRequest, OAuth2User> oAuth2UserService = new DefaultOAuth2UserService();
        OAuth2User oAuth2User = oAuth2UserService.loadUser(userRequest); // 인가 서버와 통신해서 실제 사용자 정보 조회
        OAuth2ProviderUser oAuth2ProviderUser = this.getOauth2ProviderUsers(clientRegistration, oAuth2User);

        // MemberRepository로부터 기존 회원 정보조회(responseApi 도입 후 수정 예정)
        Member member = memberRepository.findByEmail(oAuth2ProviderUser.getEmail())
                .orElseGet(() -> memberWriter.registerByOAuth2(oAuth2ProviderUser));

        log.info("구글 로그인 - 유저 아이디: {}, 유저명: {}, 이메일: {},", member.getId(), member.getUsername(), member.getEmail());


        UserInfo dto = UserInfo.from(member);
        return LikeLionOAuth2User.of(dto, oAuth2ProviderUser);
    }


    private OAuth2ProviderUser getOauth2ProviderUsers(ClientRegistration clientRegistration, OAuth2User oAuth2User) {
        String registrationId = clientRegistration.getRegistrationId();
        Map<String, Object> attributes = oAuth2User.getAttributes();
        return OAuth2ProviderUser.create(attributes, registrationId);
    }


}
