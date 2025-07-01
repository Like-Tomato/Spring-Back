package com.like_lion.tomato.domain.recruitment.entity.part;

import com.like_lion.tomato.domain.member.entity.Member;
import com.like_lion.tomato.global.common.enums.Part;
import com.like_lion.tomato.global.id.DomainId;
import com.like_lion.tomato.global.id.DomainType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class RecruimentPartQuestion {

    @DomainId(DomainType.RECRUITMENT_PART_QUESTION)
    @Id
    @Column(name = "recruitment_part_question_id")
    private String id;

    String question;

    // 필요시 기볹값 지정...해주세요
    int sortOrder;

    @Enumerated(EnumType.STRING)
    Part part;

    // 기본값 지정은 뭘로 해야할지?
    @Column(columnDefinition = "??")
    int sortedOrder;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;

    @OneToMany(mappedBy = "recruitment_part_question")
    private List<RecruimentPartAnswer> answers = new ArrayList<>();

    public void addAnswer(RecruimentPartAnswer answer) {
        answers.add(answer);
        answer.setQuestion(this);
    }
}
