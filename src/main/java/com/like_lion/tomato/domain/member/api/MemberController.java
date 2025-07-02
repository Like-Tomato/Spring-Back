package com.like_lion.tomato.domain.member.api;


import com.like_lion.tomato.domain.member.dto.request.UpdateMemberPartReq;
import com.like_lion.tomato.domain.member.dto.request.UpdateMemberProfileReq;
import com.like_lion.tomato.domain.member.dto.response.MemberProfileAssignRes;
import com.like_lion.tomato.domain.member.dto.response.MemberProfileListRes;
import com.like_lion.tomato.domain.member.dto.response.MemberProfileRes;
import com.like_lion.tomato.domain.member.entity.Member;
import com.like_lion.tomato.domain.member.service.MemberService;
import com.like_lion.tomato.global.auth.implement.JwtTokenProvider;
import com.like_lion.tomato.global.auth.service.JwtService;
import com.like_lion.tomato.global.exception.response.ApiResponse;
import com.like_lion.tomato.global.util.HttpHeaderUtil;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RequestMapping("/api/v1/member")
@RestController
public class MemberController {

    private final MemberService memberService;
    private final JwtService jwtService;

    @Operation(
            summary = "멤버 전체 목록 조회",
            description = "페이지, 사이즈, 파트, 연도별로 멤버 프로필 목록을 조회합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping
    public ApiResponse<MemberProfileListRes> readAllByFilter(
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "30") int size,
            @RequestParam(required = false) String part,
            @RequestParam(required = false) int year
    ) {
        return ApiResponse.success(memberService.readAllMemberProfiles(page, size, part, year));
    }

    @Operation(
            summary = "멤버 프로필 상세 조회",
            description = "멤버 ID로 멤버의 상세 프로필을 조회합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/{memberId}")
    public ApiResponse<MemberProfileRes> read(@PathVariable String memberId) {
        return ApiResponse.success(memberService.readMemberProfile(memberId));
    }

    @Operation(
            summary = "멤버 프로필 수정",
            description = "본인만 자신의 프로필을 수정할 수 있습니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PutMapping("/{memberId}")
    @PreAuthorize("hasRole('MEMBER') and #memberId == authentication.principal.id")
    public ApiResponse<ApiResponse.MessageData> update(@PathVariable String memberId,
                                                       @Valid @ModelAttribute UpdateMemberProfileReq request) {
        return ApiResponse.success("프로필 변경이 성공적으로 완료되었습니다.");
    }

    @Operation(
            summary = "내 프로필 조회",
            description = "현재 로그인한 사용자의 프로필 정보를 반환합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @GetMapping("/me")
    @PreAuthorize("hasRole('MEMBER')")
    public ApiResponse<MemberProfileAssignRes> readMe(HttpServletRequest request) {
        // JWT 인증을 통해 UserDetails에서 본인 memberId 추출
        String accessToken = HttpHeaderUtil.getAccessToken(request, JwtTokenProvider.BEARER_PREFIX);
        String memberId = jwtService.extractMemberIdFromAccessToken(accessToken);
        return ApiResponse.success(memberService.getMemberProfileWithAssignments(memberId));
    }

    @Operation(
            summary = "멤버 파트 수정 (관리자)",
            description = "ADMIN 권한으로 멤버의 파트(직군)를 수정합니다.",
            security = @SecurityRequirement(name = "bearerAuth")
    )
    @PatchMapping("/{memberId}/part")
    @PreAuthorize("hasRole('ADMIN')")
    public ApiResponse<ApiResponse.MessageData> updatePart(
            @PathVariable String memberId,
            @RequestBody UpdateMemberPartReq request
    ) {
        memberService.updateMemberPart(memberId, request.getPart());
        return ApiResponse.success(new ApiResponse.MessageData("파트가 성공적으로 변경되었습니다."));
    }



}

