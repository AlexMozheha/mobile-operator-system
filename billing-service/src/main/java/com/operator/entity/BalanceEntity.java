package com.operator.entity;


import jakarta.persistence.*;
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
@Table(name ="balances")
public class BalanceEntity {

    @Id
    @Column(name ="customer_id", nullable = false)
    private Long customerId;
    @Column(name ="amount", nullable = false, precision=19, scale=4)
    private BigDecimal amount = BigDecimal.ZERO;
    @Column(name ="last_updated", nullable = false)
    private Instant lastUpdated  = Instant.now();

}
