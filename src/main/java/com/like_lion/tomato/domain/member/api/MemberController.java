package com.like_lion.tomato.domain.member.api;


import com.like_lion.tomato.domain.member.dto.request.UpdateMemberProfileReq;
import com.like_lion.tomato.domain.member.dto.response.MemberProfileListRes;
import com.like_lion.tomato.domain.member.dto.response.MemberProfileRes;
import com.like_lion.tomato.domain.member.entity.Member;
import com.like_lion.tomato.domain.member.service.MemberService;
import com.like_lion.tomato.global.exception.response.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class MemberController {

    private final MemberService memberService;

    
    @GetMapping("/member")
    public ApiResponse<MemberProfileListRes> readAllByFilter(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "30") int size,
            @RequestParam(required = false) String part,
            @RequestParam(required = false) int year
    ) {
        return ApiResponse.success(memberService.readAllMemberProfiles(page, size, part, year));
    }


    @GetMapping("/{memberId}")
    public ApiResponse<MemberProfileRes> read(@PathVariable String memberId) {
        return ApiResponse.success(memberService.readMemberProfile(memberId));
    }

    @PutMapping("/{memberId}")
    @PreAuthorize("hasRole('MEMBER') and #memberId == authentication.principal.id")
    public ApiResponse<MemberProfileRes> update(@PathVariable String memberId,
                                                    @Valid @ModelAttribute UpdateMemberProfileReq request) {
        return ApiResponse.success(memberService.update(memberId, request));
    }
}

