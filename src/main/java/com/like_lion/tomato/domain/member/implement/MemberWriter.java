package com.like_lion.tomato.domain.member.implement;


import com.like_lion.tomato.domain.member.entity.Member;
import com.like_lion.tomato.domain.member.repository.MemberRepository;
import com.like_lion.tomato.global.auth.model.Role;
import com.like_lion.tomato.global.auth.model.provider.OAuth2ProviderUser;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
//import org.springframework.web.multipart.MultipartFile;

@RequiredArgsConstructor
@Component
public class MemberWriter {

    private final MemberRepository memberRepository;
    private final MemberReader memberReader;

    public Member registerByOAuth2(OAuth2ProviderUser oAuth2ProviderUser) {

        // 권한 부여 방식은 어떻게 할지 논의!(임시로 GUEST부여)
        Role role = this.isAdmin(oAuth2ProviderUser)
                ? Role.ROLE_ADMIN
                : Role.ROLE_GUEST;

        Member member = oAuth2ProviderUser.toMember(role);
        memberRepository.save(member);
        return member;
    }

    // 이후 어드민 계정 판단 방식!(최초 어드민 계정!)
    private boolean isAdmin(OAuth2ProviderUser oAuth2ProviderUser) {
        return false;
    }

    public void save(Member member) {
        memberRepository.save(member);
    }

    /** 추후 구현 예정!
    private void updateProfileImageUrl(MultipartFile profileImage, Member member) {
        fileService.deleteProfileImage(member.getProfileImageUrl());
        String newProfileImageUrl = member.getProfileImageUrl();
        member.updateProfileImageUrl(newProfileImageUrl);
    }
     **/
}
