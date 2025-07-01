package com.like_lion.tomato.domain.member.service;


import com.like_lion.tomato.domain.member.dto.request.UpdateMemberProfileReq;
import com.like_lion.tomato.domain.member.dto.response.MemberProfileListRes;
import com.like_lion.tomato.domain.member.dto.response.MemberProfileRes;
import com.like_lion.tomato.domain.member.entity.Generation;
import com.like_lion.tomato.domain.member.entity.Member;
import com.like_lion.tomato.global.common.enums.Part;
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

@RequiredArgsConstructor
@Service
public class MemberService {

    private final MemberReader memberReader;
    private final MemberWriter memberWriter;
    // private final FileUploadService fileUploadService; 의존성 추가 후 구현!


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

        Page<Member> memberPage = memberReader.findAllByPartAndYear(partEnum, yearInteger, pageable);

        // Member -> DTO
        return MemberProfileListRes.from(
                memberPage.getContent()
                        .stream()
                        .map(MemberProfileRes::from)
                        .toList()
        );
    }

    @Transactional
    public MemberProfileRes readMemberProfile(String memberId) {
        Member member =  memberReader.findOptionById(memberId)
                .orElseThrow(
                        () -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND)
                );
        return MemberProfileRes.from(member);
    }

    @Transactional
    public MemberProfileRes update(String memberId, UpdateMemberProfileReq request) {
        Member member = memberReader.findOptionById(memberId)
                .orElseThrow(
                        () -> new MemberException(MemberErrorCode.MEMBER_NOT_FOUND)
                );
        // 프로필 이미지 업로드 로직 구현!
        String profileUrl = member.getProfileUrl();
        if(profileUrl != null && !profileUrl.isEmpty()) {
            // profileUrl = fileUploadService.upload(request.getProfileImg());
            // 의존성 추가 구현 후 과련 예외처리도 진행!
        }

        //member.update();
        memberWriter.save(member);
        return MemberProfileRes.from(member);
    }
}
