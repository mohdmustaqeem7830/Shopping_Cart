package com.ecom.Shopping_Cart.Model;


import jakarta.persistence.*;
import lombok.*;
import org.springframework.web.bind.annotation.GetMapping;
@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;
    @ManyToOne
    private UserDtls user;
    @ManyToOne
    private Product product;
    private Integer quantity;

    @Transient
    private Double totalPrice;
    @Transient
    private Double totalOrderPrice;
}
