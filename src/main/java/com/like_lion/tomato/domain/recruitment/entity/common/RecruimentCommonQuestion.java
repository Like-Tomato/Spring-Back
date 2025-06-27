package com.like_lion.tomato.domain.recruitment.entity.common;

import com.like_lion.tomato.domain.member.entity.Member;
import com.like_lion.tomato.global.id.DomainId;
import com.like_lion.tomato.global.id.DomainType;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;


@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class RecruimentCommonQuestion {

    @DomainId(DomainType.RECRUITMENT_COMMON_QUESTION)
    @Id
    @Column(name = "recruitment_common_question_id")
    private String id;

    String question;
    int sortedOrder;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id")
    private Member member;


    @OneToMany(mappedBy = "recruitment_common_question")
    private List<RecruimentCommonAnswer> answers = new ArrayList<>();

    public void addAnswer(RecruimentCommonAnswer answer) {
        this.answers.add(answer);
        answer.setQuestion(this);

    }
}
