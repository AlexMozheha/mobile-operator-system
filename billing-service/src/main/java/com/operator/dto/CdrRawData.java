package com.operator.dto;

import com.operator.enums.CallDirection;
import com.operator.enums.CallReleaseCause;

import java.time.Instant;

public record CdrRawData(
        String callId,
        String sessionId,

        // Номери
        String callingNumber,   // A-number
        String calledNumber,    // B-number

        // Таймінги
        Instant callStartTime,
        Instant callAnswerTime,
        Instant callEndTime,

        // Тривалість (як прийшла з мережі)
        Long durationSeconds,

        // Статус дзвінка
        CallReleaseCause releaseCause,
        CallDirection direction
) {
}
