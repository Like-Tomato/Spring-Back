package com.like_lion.tomato.domain.recruitment.dto.applicant;

import java.util.List;
import java.util.Map;

public record PassResponse(
        int round,
        int totalCount,
        Map<String, List<ApplicantResponse.Simple>> simpleInfos
) {
}
