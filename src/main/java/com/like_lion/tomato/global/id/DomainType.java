package com.like_lion.tomato.global.id;

import lombok.Getter;

@Getter
public enum DomainType {
    MEMBER("MEMBER"),
    GENERATION("GENERATION"),
    APPLICATION("APPLICATION"),
    RECRUITMENT_COMMON_ANSWER("RECRUIMENT_COMMON_ANSWER"),
    RECRUITMENT_COMMON_QUESTION("RECRUIMENT_COMMON_QUESTION"),
    RECRUITMENT_PART_ANSWER("RECRUITMENT_PART_ANSWER"),
    RECRUITMENT_PART_QUESTION("RECRUITMENT_PART_QUESTION"),
    PROJECT("PROJECT"),
    PROJECT_IMAGE("PROJECT_IMAGE"),
    GALLERY("GALLERY"),
    GALLERY_IMAGE("GALLERY_IMAGE"),
    SESSION("SESSION"),
    SESSION_FILE("SESSION_FILE"),
    SESSION_QUIZ("SESSION_QUIZ"),
    QUIZ_QUESTION("QUIZ_QUESTIONO"),
    QUIZ_OPTION("QUIZ_OPTIONO"),
    QUIZ_ANSWER("QUIZ_ANSWER"),
    ASSIGNMENT("ASSIGNMENT"),
    ASSIGNMENT_SUBMISSION("ASSIGNMENT_SUBMISSION"),
    REFRESH_TOCKEN("REFRESH_TOCKEN"),;

    private final String prefix;

    DomainType(String prefix) {
        this.prefix = prefix;
    }
}
