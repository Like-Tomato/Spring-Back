package com.like_lion.tomato.domain.archieve.entity.project;
import com.like_lion.tomato.global.id.DomainId;
import com.like_lion.tomato.global.id.DomainType;
import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class ProjectImage {

    @DomainId(DomainType.PROJECT_IMAGE)
    @Id
    @Column(name = "project_image_id")
    private String id;

    private String imageUrl;

    private int setOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project")
    private Project project;
}
