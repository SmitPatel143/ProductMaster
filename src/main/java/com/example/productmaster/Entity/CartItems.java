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

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "cartItemProductId")
    private Collection<Product> products;

    private int quantity;

    private Double price;
}
