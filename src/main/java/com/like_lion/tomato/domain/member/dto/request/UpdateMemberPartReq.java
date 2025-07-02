package com.like_lion.tomato.domain.member.dto.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Getter
public class UpdateMemberPartReq {
    @JsonProperty("part")
    private String part;
}
