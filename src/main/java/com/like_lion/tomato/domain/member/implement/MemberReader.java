package com.like_lion.tomato.domain.member.implement;

import com.like_lion.tomato.domain.member.entity.Member;
import com.like_lion.tomato.global.common.enums.Part;
import com.like_lion.tomato.domain.member.exception.MemberErrorCode;
import com.like_lion.tomato.domain.member.exception.MemberException;
import com.like_lion.tomato.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Component
public class MemberReader {

    private final MemberRepository memberRepository;

    public Optional<Member> findOptionalByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public Page<Member> findAllByPartAndYear(Part part, Integer year, Pageable pageable) {
        // 권한이 MEMBER 이상인 멤버만 조회
        Page<Member> memberPage = memberRepository.findAllByPartAndYearAndRoleIn(part, year, pageable);
        // 기수가 없는 멤버는 제거, 최신 기수만 남기기
        List<Member> filteredMembers = memberPage.getContent().stream()
                .filter(member -> member.getLatestYear() != null)
                .collect(Collectors.toList());
        // Page 객체로 재생성 (실무에서는 QueryDSL로 한 번에 처리하는 게 더 좋음)
        return new PageImpl<>(filteredMembers, pageable, memberPage.getTotalElements());
    }

    public Optional<Member> findOptionById(String id) {
        return memberRepository.findById(id);
    }

    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
    }

}
