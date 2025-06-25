package com.like_lion.tomato.global.id;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface SequenceCounterRepository extends JpaRepository<SequenceCounter, String> {
    Optional<SequenceCounter> findByDomainType(DomainType domainType);
}