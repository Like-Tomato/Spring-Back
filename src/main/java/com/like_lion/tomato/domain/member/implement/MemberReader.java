package com.like_lion.tomato.domain.member.implement;

import com.like_lion.tomato.domain.member.entity.Member;
import com.like_lion.tomato.domain.member.entity.Part;
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

    // findById, findByUsername, findByPart 등등 필요시 추가 구현 예정


}
