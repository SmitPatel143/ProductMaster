package com.example.productmaster.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

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
    @JsonIgnore
    private MyUser user;

    @OneToMany(mappedBy = "cart")
    @JsonIgnore
    private List<CartItems> cartItems;

    public Cart(MyUser user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Cart{" +
                "id=" + id +
                ", user=" + user.getEmail() +
                '}';
    }
}
