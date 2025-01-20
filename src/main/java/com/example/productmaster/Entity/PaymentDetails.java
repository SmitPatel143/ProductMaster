package com.example.productmaster.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PaymentDetails_Id_Generator")
    @SequenceGenerator(name = "PaymentDetails_Id_Generator", allocationSize = 1)
    private Long id;

    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;

    private String paymentReferenceId;

    private String paymentTransactionNumber;

    private String paymentStatus;

    private LocalDateTime paymentDate;

    private String paymentGateway;


}
