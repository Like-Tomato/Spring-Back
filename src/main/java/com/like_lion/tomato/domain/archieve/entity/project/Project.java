package com.like_lion.tomato.domain.archieve.entity.project;

import com.like_lion.tomato.domain.member.entity.Generation;
import com.like_lion.tomato.domain.member.entity.Member;
import com.like_lion.tomato.global.id.DomainId;
import com.like_lion.tomato.global.id.DomainType;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class Project {

    @DomainId(DomainType.PROJECT)
    @Id
    @Column(name = "project_id")
    private String id;

    @Column(nullable = false, length = 30)
    private String title;

    @Column(nullable = false, length = 200)
    private String description;

    @Column(nullable = false, columnDefinition = "BOOLEAN DEFAULT FALSE" )
    private boolean isBest;

    private String projectUrl;

    private String thumbnailUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "generation_id")
    private Generation generation;
}
