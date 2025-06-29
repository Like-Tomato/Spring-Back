package com.like_lion.tomato.domain.member.api;


import com.like_lion.tomato.domain.member.dto.response.MemberProfileListRes;
import com.like_lion.tomato.domain.member.service.MemberService;
import com.like_lion.tomato.global.exception.response.ApiResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class MemberController {

    private final MemberService memberService;

    @GetMapping("/member")
    public ApiResponse<MemberProfileListRes> getMemberProfiles(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "30") int size,
            @RequestParam(required = false) String part,
            @RequestParam(required = false) int year
    ) {
        MemberProfileListRes response = memberService.readAllMemberProfiles(page, size, part, year);
        return ApiResponse.success(response);
    }
}

