package com.example.productmaster.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Category_Id_Generator")
    @SequenceGenerator(name = "UserDetails_Id_Generator", allocationSize = 1)
    private Long id;

    private String categoryId;

    private String name;

    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;
}
