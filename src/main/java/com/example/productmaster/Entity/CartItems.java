package com.example.productmaster.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Collection;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class CartItems {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "CartItems_Id_Generator")
    @SequenceGenerator(name = "CartItems_Id_Generator", allocationSize = 1)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private Product product;

    @ManyToOne
    private Cart cart;

    private int quantity;

    private Double totalPrice;


    public CartItems(Product product, int quantity, Double totalPrice, Cart cart) {
        this.product = product;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
        this.cart =cart;
    }

    @Override
    public String toString() {
        return "CartItems{" +
                "id=" + id +
                ", product=" + product.toString() +
                ", quantity=" + quantity +
                ", totalPrice=" + totalPrice +
                '}';
    }
}
