package com.like_lion.tomato.domain.session.service;

import com.like_lion.tomato.domain.session.dto.SessionListRes;
import com.like_lion.tomato.domain.session.dto.response.SessionSimpleRes;
import com.like_lion.tomato.domain.session.entity.session.Session;
import com.like_lion.tomato.domain.session.entity.session.Session.Part;
import com.like_lion.tomato.domain.session.repository.SessionRepository;
import com.like_lion.tomato.domain.session.exception.SessionErrorCode;
import com.like_lion.tomato.domain.session.exception.SessionException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@RequiredArgsConstructor
@Service
public class SessionService {

    private final SessionRepository sessionRepository;

    @Transactional(readOnly = true)
    public SessionListRes readAllSessions(String part) {
        Part partEnum = null;
        if (part != null && !part.isBlank()) {
            if (!Part.isValid(part)) {
                throw new SessionException(SessionErrorCode.INVALID_PART);
            }
            partEnum = Part.valueOf(part.toUpperCase());
        }
        List<Session> sessionEntities = sessionRepository.findAllByPartOrAll(partEnum);

        List<SessionSimpleRes> simpleResList = sessionEntities.stream()
                .map(SessionSimpleRes::from)
                .toList();

        return SessionListRes.from(simpleResList);
    }
}
