package com.operator.entity;


import com.operator.enums.InvoiceStatus;
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
@Table(name="invoices")
public class InvoiceEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name ="customer_id", nullable = false)
    private Long customerId;
    @Column(name="amount", nullable = false, precision=19, scale=4)
    private BigDecimal amount;
    @Column(name="issue_date", nullable = false)
    private Instant issueDate  = Instant.now();
    @Column(name="paid_at")
    private Instant paidAt;
    @Enumerated(EnumType.STRING)
    @Column(name="status", nullable = false)
    private InvoiceStatus invoiceStatus = InvoiceStatus.UNPAID;

}
