package com.like_lion.tomato.domain.archive.project.entity;
import com.like_lion.tomato.global.id.DomainId;
import com.like_lion.tomato.global.id.DomainType;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "project_images")
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ProjectImage {

    @DomainId(DomainType.PROJECT_IMAGE)
    @Id
    @Column(name = "project_image_id")
    private String id;

    private String imageUrl;
    private String fileKey;
    private int setOrder;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    public void updateImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}
