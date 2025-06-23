package com.like_lion.tomato.domain.member.service;


import com.like_lion.tomato.domain.member.implement.MemberReader;
import com.like_lion.tomato.domain.member.implement.MemberWriter;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class MemerService {

    private final MemberReader memberReader;
    private final MemberWriter memberWriter;


}
