package com.example.productmaster.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;

@Entity
@NoArgsConstructor
@Data
public class OrderItems {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OrderItems_Id_Generator")
    @SequenceGenerator(name = "OrderItems_Id_Generator", allocationSize = 1)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    @JsonIgnore
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private BigDecimal unitPrice;

    @Column(nullable = false)
    private BigDecimal totalPrice;


    public OrderItems(Product product, int quantity,BigDecimal totalPrice) {
        this.product = product;
        this.quantity = quantity;
        this.unitPrice = BigDecimal.valueOf(product.getSalesPrice());
        this.totalPrice = totalPrice;
    }

    @PrePersist
    @PreUpdate
    protected void calculateTotalPrice() {
        this.totalPrice = this.unitPrice.multiply(BigDecimal.valueOf(this.quantity));
    }
}
