package com.operator.entity;


import jakarta.persistence.*;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="usage_records")
public class UsageRecordEntity {

// Залишок від використаного користувачем
    @Id
    @Column(name = "customer_id", nullable = false)
    @PositiveOrZero private Long customerId;
    @Column(name = "call_minutes", nullable = false)
    @PositiveOrZero private Integer callMinutes;
    @Column(name ="sms_count", nullable = false)
    @PositiveOrZero private Integer smsCount;
    @Column(name = "internet_count", nullable = false, precision = 4, scale = 1)
    private BigDecimal internetCount;
    @Column(name ="usage_date", nullable = false)
    private Instant usageDate = Instant.now();


}
