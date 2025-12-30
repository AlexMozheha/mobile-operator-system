package com.operator.enums;

public enum CallReleaseCause {
    NORMAL_CLEARING,

    // Клієнтська поведінка
    BUSY,
    NO_ANSWER,
    REJECTED,

    // Мережеві причини
    NETWORK_ERROR,
    CONGESTION,
    TEMPORARY_FAILURE,

    // Системні
    TIMEOUT,
    SYSTEM_FAILURE,

    UNKNOWN
}
