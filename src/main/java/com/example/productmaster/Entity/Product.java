package com.example.productmaster.Entity;

import com.example.productmaster.DTO.ProductDto;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
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
    private String wsCode;

    private String name;

    private String description;

    @OneToMany(mappedBy = "product", fetch = FetchType.LAZY)
    private List<ProductImages> productImagesList;

    private float salesPrice;

    @Column(name = "mrp")
    private int mrp;

    private int quantity;

    private float packageSize;

    private boolean activeStatus;

    @ManyToOne(cascade = CascadeType.ALL)
    private Category category;


    public Product(String name, String description, List<ProductImages> images, float salesPrice, int MRP, int quantity, float packageSize, Category category) {
        this.name = sanitizeMedicineName(name);
        this.description = description;
        this.productImagesList = images;
        this.salesPrice = salesPrice;
        this.mrp = MRP;
        this.packageSize = packageSize;
        this.category = category;
        this.wsCode = wsCodeGenerator(this.name);
        this.activeStatus = true;
        this.quantity = quantity;
    }

    public Product(ProductDto productDto, Product product, Category category) {
        this.id = product.getId();
        this.name = productDto.getName() == null ? product.getName() : productDto.getName();
        this.description = productDto.getDescription() == null ? product.getDescription() : productDto.getDescription();
        this.activeStatus = productDto.isActiveStatus();
        this.mrp = productDto.getMrp();
        this.quantity = productDto.getQuantity();
        this.packageSize = productDto.getPackageSize();
        this.salesPrice = productDto.getSalesPrice();
        this.category = category;
    }



    private String wsCodeGenerator(final String medicineName) {
        String sanitizedMedicineName = sanitizeMedicineName(sanitizeMedicineName(medicineName));
        String timestamp = new SimpleDateFormat("yyyyMMdd").format(new Date());
        String uniqueSuffix = UUID.randomUUID().toString().substring(0, 8);
        return "MED-" + sanitizedMedicineName + "-" + timestamp + "-" + uniqueSuffix;
    }

    private String sanitizeMedicineName(final String medicineName) {
        return medicineName.toUpperCase().replaceAll("[^A-Za-z0-9]", "_");
    }

    @Override
    public String toString() {
        return "Product{" +
                "id=" + id +
                ", wsCode='" + wsCode + '\'' +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", salesPrice=" + salesPrice +
                ", MRP=" + mrp +
                ", quantity=" + quantity +
                ", packageSize=" + packageSize +
                ", activeStatus=" + activeStatus +
                ", category=" + category.toString() +
                ", imageURL="+ productImagesList.getFirst().getImageURL()+
                '}';
    }
}
