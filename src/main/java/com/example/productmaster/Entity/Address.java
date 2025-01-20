package com.example.productmaster.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Address_Id_Generator")
    @SequenceGenerator(name = "Address_Id_Generator", allocationSize = 1)
    private Long id;

    private String street;

    private String locality;

    private String city;

    private String state;

    private String zip;

    private String country;

    private String phone;

}
