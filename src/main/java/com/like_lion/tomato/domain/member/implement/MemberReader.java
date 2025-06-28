package com.like_lion.tomato.domain.member.implement;

import com.like_lion.tomato.domain.member.entity.Member;
import com.like_lion.tomato.domain.member.entity.Part;
import com.like_lion.tomato.domain.member.exception.MemberErrorCode;
import com.like_lion.tomato.domain.member.exception.MemberException;
import com.like_lion.tomato.domain.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import java.util.Optional;

@RequiredArgsConstructor
@Component
public class MemberReader {

    private final MemberRepository memberRepository;

    public Optional<Member> findOptionalByEmail(String email) {
        return memberRepository.findByEmail(email);
    }

    public Page<Member> findAllByPartAndYear(Part part, Integer year, Pageable pageable) {
        return memberRepository.findAllByPartAndYear(part, year, pageable);
    }

    public Optional<Member> findOptionById(String id) {
        return memberRepository.findById(id);
    }

    public Member findByUsername(String username) {
        return memberRepository.findByUsername(username)
                .orElseThrow(() -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND));
    }
    // 필요시 checkExistUsername 구현!
}
