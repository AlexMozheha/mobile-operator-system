package com.operator.entity;


import com.operator.dto.enums.CustomerStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Table(name = "customers")
public class CustomerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "first_name", nullable = false) private String firstName;
    @Column(name = "last_name", nullable = false) private String lastName;
    @Column(name = "phone_number", nullable = false, unique = true) private String phoneNumber;
    @Column(name = "email", unique = true) private String email;
    @Column(name = "tariff_id", nullable = false) private Long tariffId;
    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false) private CustomerStatus status = CustomerStatus.ACTIVE;

}
