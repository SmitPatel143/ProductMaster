package com.example.productmaster.Entity;

import com.example.productmaster.DTO.CategoryDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.checkerframework.common.aliasing.qual.Unique;

import java.util.List;
import java.util.Random;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Category_Id_Generator")
    @SequenceGenerator(name = "Category_Id_Generator", allocationSize = 1)
    private Long id;

    private String categoryId;

    @Column(unique = true)
    private String name;

    private String description;

    private boolean active;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    @JsonIgnore
    private List<Product> productList;

    public Category(String name, String description) {
        this.categoryId = generateCategoryId();
        this.name = name;
        this.description = description;
    }

    public Category(CategoryDto categoryDto) {
        this.name = categoryDto.getName();
        this.description = categoryDto.getDescription();
        this.categoryId = generateCategoryId();
        this.active = categoryDto.isActive();
    }

    private String generateCategoryId() {
        return "CAT" + this.getName().substring(0, 3).toUpperCase() + getRandomString();
    }

    private String getRandomString() {
        Random rand = new Random();
        int randomNum = rand.nextInt(1000, 9999);  // Generate a random number between 1000 and 9999
        return String.valueOf(randomNum);
    }

    @Override
    public String toString() {
        return "Category{" +
                "id=" + id +
                ", categoryId='" + categoryId + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                '}';
    }


}
