package com.like_lion.tomato.domain.session.entity.assignment;

import com.like_lion.tomato.global.id.DomainId;
import com.like_lion.tomato.global.id.DomainType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
public class Assignment {

    @DomainId(DomainType.ASSIGNMENT)
    @Id
    @Column(name = "assignment_id")
    private String id;

    private String title;

    private String description;

    private Date started_at;

    private Date ended_at;

}
