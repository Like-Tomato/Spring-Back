package com.like_lion.tomato.domain.member.entity;

import jakarta.persistence.*;

import java.sql.Array;
import java.util.ArrayList;

@Entity
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;
    private String email;
    private String profileImageUrl;
    private String phone;
    private String birth;
    private String role;
    private String part;
    private String introduce;
    // 구글 로그인만 진행하므로 provider, provider_id 필드는 생략, 만약 네이버, 카카오 로그인 확장시 추가 구현

    // 과제 구현 후 양방향 매핑 예정
    // my_page에 과제칸이 존재하므로 즉시조회 예정!
    //@OneToMany(mappedBy = "member", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    //private List<Assignment> assignments = new ArrayList<>();

    // 연관관계 편의 메서드
    /**
    public void addassignment(Assignment assignment) {
        assignments.add(assignment);
        assignment.setMember(this);
    }
     **/

}
