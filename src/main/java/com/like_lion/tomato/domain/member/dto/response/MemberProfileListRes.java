package com.like_lion.tomato.domain.member.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class MemberProfileListRes {
    private final List<MemberProfileRes> members;

    public MemberProfileListRes(List<MemberProfileRes> members) {
        this.members = members;
    }

    // 간소화된 from 메서드
    public static MemberProfileListRes from(List<MemberProfileRes> members) {
        return new MemberProfileListRes(members);
    }
}