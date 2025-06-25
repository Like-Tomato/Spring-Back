package com.like_lion.tomato.domain.member.service;


import com.like_lion.tomato.domain.member.dto.response.MemberProfileListRes;
import com.like_lion.tomato.domain.member.dto.response.MemberProfileRes;
import com.like_lion.tomato.domain.member.entity.Member;
import com.like_lion.tomato.domain.member.implement.MemberReader;
import com.like_lion.tomato.domain.member.implement.MemberWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberReader memberReader;
    private final MemberWriter memberWriter;


    public MemberProfileListRes readAllMemberProfiles(int page, int size, String part, int year) {
        // 페이지네이션 및 정렬(CreatedAt 기준 내림차순)
        PageRequest pageable = PageRequest.of(page - 1,
                size,
                Sort.by("CreatedAt").descending()); //반드시 SuperMapped로 생성 시각 만들기

        //필터링 적용
        Page<Member> memberPage = memberReader.findAllByPartAndYear(part, year, pageable);

        // Member -> DTO
        List<MemberProfileRes> memberProfiles = memberPage.getContent()
                .stream()
                .map(MemberProfileRes::from)
                .toList();

        // 임시 필터 정보 (실제로는 DB 집계 쿼리 필요)
        List<MemberProfileListRes.PositionCount> positions = List.of(
                new MemberProfileListRes.PositionCount("FRONTEND", 12),
                new MemberProfileListRes.PositionCount("BACKEND", 8)
        );
        List<MemberProfileListRes.SkillCount> skills = List.of(
                new MemberProfileListRes.SkillCount("React", 15),
                new MemberProfileListRes.SkillCount("Node.js", 10)
        );

        return MemberProfileListRes.from(
                memberProfiles,
                page,
                size,
                memberPage.getTotalElements(),
                memberPage.getTotalPages(),
                positions,
                skills
        );
    }
}
