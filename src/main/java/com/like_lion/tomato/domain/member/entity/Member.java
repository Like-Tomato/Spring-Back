package com.like_lion.tomato.domain.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.sql.Array;
import java.util.ArrayList;

@Getter

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 20)
    private String username;

    @Column(nullable = false, unique = true, length = 30)
    private String email;

    private String profileImageUrl;

    // @Column(EnumType.STRING)
    // private Role role; // ROLE만든 후 구현!
    @Column(nullable = false, length = 30)
    private String part;

    @Column(nullable = false, length = 200)
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
