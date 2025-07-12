package com.like_lion.tomato.global.id;

import jakarta.persistence.PrePersist;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;

@Component
public class IdGeneratorListener {
    public static IdGenerator idGenerator;

    @Autowired
    public void setIdGenerator(IdGenerator generator) {
        IdGeneratorListener.idGenerator = generator;
    }

    @PrePersist
    public void onPrePersist(Object entity) {
        if (idGenerator == null) {
            return;
        }

        Arrays.stream(entity.getClass().getDeclaredFields())
                .filter(field -> field.isAnnotationPresent(DomainId.class))
                .forEach(field -> {
                    try {
                        field.setAccessible(true);
                        if (field.get(entity) == null) {
                            DomainType domainType = field.getAnnotation(DomainId.class).value();
                            String generatedId = idGenerator.generateId(domainType);
                            field.set(entity, generatedId);
                        }
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("ID 생성 중 오류 발생", e);
                    }
                });
    }
}
