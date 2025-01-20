package com.example.productmaster.Entity;

import jakarta.persistence.*;

@Entity
public class OrderItems {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "OrderItems_Id_Generator")
    @SequenceGenerator(name = "OrderItems_Id_Generator", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "order_id", referencedColumnName = "id")
    private Order order;

    @OneToOne(cascade = CascadeType.MERGE)
    private Product product;

    private int quantity;

    private double unitPrice;

    @Transient
    private double getTotalPrice() {
        return unitPrice * quantity;
    }



}
