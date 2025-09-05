package com.producthor.Producthor.domain.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "users",
        uniqueConstraints = {
                @UniqueConstraint(name = "uk_users_username", columnNames = "username")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User extends BaseEntity {

    @NotBlank
    @Column(nullable = false, length = 64)
    private String username;

    @NotBlank
    @Column(nullable = false)
    private String password;

    @Column(length = 128)
    private String name;

    @Column(nullable = false)
    private boolean isAdmin;

    @OneToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(
            name = "shipping_data_id",
            foreignKey = @ForeignKey(name = "fk_users_shipping_data"),
            unique = true
    )
    private ShippingData shippingData;

    @OneToMany(mappedBy = "user", fetch = FetchType.LAZY)
    @Builder.Default
    @JsonManagedReference
    private List<Order> orderHistory = new ArrayList<>();
}