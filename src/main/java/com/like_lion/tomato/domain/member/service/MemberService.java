package com.like_lion.tomato.domain.member.service;


import com.like_lion.tomato.domain.member.dto.response.MemberProfileListRes;
import com.like_lion.tomato.domain.member.dto.response.MemberProfileRes;
import com.like_lion.tomato.domain.member.entity.Generation;
import com.like_lion.tomato.domain.member.entity.Member;
import com.like_lion.tomato.domain.member.entity.Part;
import com.like_lion.tomato.domain.member.exception.MemberErrorCode;
import com.like_lion.tomato.domain.member.exception.MemberException;
import com.like_lion.tomato.domain.member.implement.MemberReader;
import com.like_lion.tomato.domain.member.implement.MemberWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberReader memberReader;
    private final MemberWriter memberWriter;


    @Transactional
    public MemberProfileListRes readAllMemberProfiles(int page, int size, String part, int year) {

        // 유효성 검사(나중에 Valid로 리팩터링 예정)
        if(!part.isBlank() && !Part.isValid(part)) throw new MemberException(MemberErrorCode.INVALID_PART);
        Part partEnum = Part.valueOf(part.toUpperCase());

        if(!Generation.isValidYear(year)) throw new MemberException(MemberErrorCode.INVALID_YEAR);

        // MemberProfileRequest에서 Valid로 유효성 검사 구현!
        Integer yearInteger = year;

        // 페이지네이션 및 정렬(CreatedAt 기준 내림차순)
        PageRequest pageable = PageRequest.of(page - 1,
                size,
                Sort.by("CreatedAt").descending());

        // part, year은 Generation에 있으므로 MemberGeneration에서 JOIN하여 필터링
        Page<Member> memberPage = memberReader.findAllByPartAndYear(partEnum, yearInteger, pageable);

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
