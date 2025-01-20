package com.example.productmaster.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.cglib.core.Local;

import java.time.LocalDateTime;
import java.util.Collection;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Order_Id_Generator")
    @SequenceGenerator(name = "Order_Id_Generator", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", referencedColumnName = "id")
    private MyUser user;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "address_id",  referencedColumnName = "id")
    private Address address;

    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL, orphanRemoval = true)
    private Collection<OrderItems> orderItems;

    private LocalDateTime orderDate;

    private float totalAmount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;


    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    private LocalDateTime updatedAt;

    @OneToOne
    @JoinColumn(name = "paymentDetails_id", referencedColumnName = "id")
    private PaymentDetails paymentDetails;

    @PrePersist
    protected void onCreate() {
        this.orderDate = LocalDateTime.now();
        this.updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
