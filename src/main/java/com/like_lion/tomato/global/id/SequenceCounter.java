package com.like_lion.tomato.global.id;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sequence_counters")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Getter
@Setter
public class SequenceCounter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    @Enumerated(EnumType.STRING)
    private DomainType domainType;

    private long last;
}
