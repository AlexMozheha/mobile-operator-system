package com.operator.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name="tariffs")
public class TariffEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="id")
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @Column(name = "monthly_price",  nullable = false, precision = 19, scale = 4)
    private BigDecimal monthlyPrice;
    @Column(name ="minutes_package", nullable = false)
    private Integer minutesPackage;
    @Column(name="sms_package", nullable = false)
    private Integer smsPackage;
    @Column(name = "gb_package", nullable = false, precision = 4, scale = 1)
    private BigDecimal gbPackage;


}
