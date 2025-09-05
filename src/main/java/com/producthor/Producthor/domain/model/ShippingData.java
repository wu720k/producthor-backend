package com.producthor.Producthor.domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

@Entity
@Table(name = "shipping_data")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShippingData extends BaseEntity {

    @NotBlank
    @Column(nullable = false, length = 16)
    private String postalCode;

    @NotBlank
    @Column(nullable = false, length = 64)
    private String city;

    @NotBlank
    @Column(nullable = false, length = 128)
    private String street;

    @NotBlank
    @Column(nullable = false, length = 16)
    private String houseNumber;

    @Column(length = 256)
    private String additionalInfo;
}