package com.producthor.Producthor.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import lombok.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Product extends BaseEntity {

    @NotBlank
    @Column(nullable = false, length = 128)
    private String name;

    @Column(columnDefinition = "text")
    private String description;

    @Positive
    @Column(nullable = false, precision = 12, scale = 2)
    private BigDecimal price;

    @Column(name = "image_url", length = 512)
    private String imageUrl;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id",
            foreignKey = @ForeignKey(name = "fk_products_category"))
    @JsonBackReference(value = "category-products")
    private Category category;

    @ElementCollection(fetch = FetchType.EAGER)
    @CollectionTable(
            name = "product_specifications",
            joinColumns = @JoinColumn(name = "product_id", foreignKey = @ForeignKey(name = "fk_spec_product"))
    )
    @MapKeyColumn(name = "spec_key", length = 64)
    @Column(name = "spec_value", length = 256)
    @Builder.Default
    private Map<String, String> specifications = new HashMap<>();

    @Column(nullable = false)
    @Builder.Default
    private boolean available = true;
}
