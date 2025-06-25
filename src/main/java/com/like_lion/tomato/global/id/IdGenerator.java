package com.like_lion.tomato.global.id;

import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;


import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

@Component
public class IdGenerator {
    private final SequenceCounterRepository sequenceCounterRepository;
    private final Map<DomainType, AtomicLong> counters = new ConcurrentHashMap<>();

    public IdGenerator(SequenceCounterRepository sequenceCounterRepository) {
        this.sequenceCounterRepository = sequenceCounterRepository;
        for (DomainType type : DomainType.values()) {
            counters.put(type, new AtomicLong(loadLastValue(type)));
        }
    }

    private long loadLastValue(DomainType domainType) {
        return sequenceCounterRepository.findByDomainType(domainType)
                .map(SequenceCounter::getLast)
                .orElse(0L);
    }

    @Transactional
    public String generateId(DomainType domainType) {
        String prefix = domainType.getPrefix();

        AtomicLong counter = counters.get(domainType);
        long sequence = counter.incrementAndGet();

        updateSequence(domainType, sequence);

        return prefix + sequence;
    }

    private void updateSequence(DomainType domainType, long newValue) {
        SequenceCounter counter = sequenceCounterRepository.findByDomainType(domainType)
                .orElseGet(() -> {
                    SequenceCounter newCounter = new SequenceCounter();
                    newCounter.setDomainType(domainType);
                    newCounter.setLast(0L);
                    return newCounter;
                });

        counter.setLast(newValue);
        sequenceCounterRepository.save(counter);
    }
}