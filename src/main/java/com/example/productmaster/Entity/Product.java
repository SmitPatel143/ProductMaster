package com.example.productmaster.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import java.util.UUID;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Product_Id_Generator")
    @SequenceGenerator(name = "Product_Id_Generator", allocationSize = 1)
    private Long id;

    @Column(unique = true)
    private String productId;

    private String name;

    private String description;

    private String imageURL;

    private float salesPrice;

    private float MRP;

    private int quantity;

    private float packageSize;


    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL)
    private List<Category> categoryList;

    public Product(String name, String description, String imageURL, float salesPrice, float packageSize, float MRP, List<Category> categoryList) {
        this.name = name;
        this.description = description;
        this.imageURL = imageURL;
        this.salesPrice = salesPrice;
        this.MRP = MRP;
        this.packageSize = packageSize;
        this.categoryList = categoryList;
        this.productId = productIdGenerator();
    }

    private String productIdGenerator() {
        try {
            String input = this.getName() + this.getMRP() + this.getQuantity() + UUID.randomUUID();
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));

            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.substring(0, 16);

        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error generating unique ID", e);
        }
    }
}
