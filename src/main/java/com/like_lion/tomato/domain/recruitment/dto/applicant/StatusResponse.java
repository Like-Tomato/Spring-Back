package com.like_lion.tomato.domain.recruitment.dto.applicant;

import java.util.List;

public record StatusResponse(
        int round,
        String part,
        List<ApplicantResponse> applicantResponse
) {
}
