package com.like_lion.tomato.domain.member.dto.response;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
public class MemberProfileListRes {

    private final List<MemberProfileRes> members;
    private final Pagination pagination;
    private final Filters filters;

    // 생성자 (외부에서 직접 사용하지 않음)
    private MemberProfileListRes(List<MemberProfileRes> members, Pagination pagination, Filters filters) {
        this.members = members;
        this.pagination = pagination;
        this.filters = filters;
    }

    // 정적 팩토리 메서드
    public static MemberProfileListRes from(
            List<MemberProfileRes> members,
            int page,
            int size,
            long total,
            int totalPages,
            List<PositionCount> positions,
            List<SkillCount> skills
    ) {
        return new MemberProfileListRes(
                members,
                new Pagination(page, size, total, totalPages),
                new Filters(positions, skills)
        );
    }

    @Getter
    public static class Pagination {
        private final int page;
        private final int size;
        private final long total;
        private final int totalPages;

        public Pagination(int page, int size, long total, int totalPages) {
            this.page = page;
            this.size = size;
            this.total = total;
            this.totalPages = totalPages;
        }
    }

    @Getter
    public static class Filters {
        private final List<PositionCount> positions;
        private final List<SkillCount> skills;

        public Filters(List<PositionCount> positions, List<SkillCount> skills) {
            this.positions = positions;
            this.skills = skills;
        }
    }

    @Getter
    public static class PositionCount {
        private final String name;
        private final int count;

        public PositionCount(String name, int count) {
            this.name = name;
            this.count = count;
        }
    }

    @Getter
    public static class SkillCount {
        private final String name;
        private final int count;

        public SkillCount(String name, int count) {
            this.name = name;
            this.count = count;
        }
    }
}
