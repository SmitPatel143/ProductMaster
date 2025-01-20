package com.example.productmaster.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ProductImages {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "ProductImages_Id_Generator")
    @SequenceGenerator(name = "ProductImages_Id_Generator", allocationSize = 1)
    private Long id;

    private String imageURL;

    @ManyToOne
    @JsonIgnore
    private Product product;

    public ProductImages(String imageURL) {
        this.imageURL = imageURL;
    }

    @Override
    public String toString() {
        return "ProductImages{" +
                "id=" + id +
                ", imageURL='" + imageURL + '\'' +
                '}';
    }
}
