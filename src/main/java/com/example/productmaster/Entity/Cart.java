package com.example.productmaster.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "Cart_Id_Generator")
    @SequenceGenerator(name = "Cart_Id_Generator", allocationSize = 1)
    private Long id;

    @OneToOne
    private MyUser user;


}
