package com.producthor.Producthor.domain.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_items",
        indexes = {
                @Index(name = "idx_order_items_order_id", columnList = "order_id")
        })
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItem extends BaseEntity {

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "order_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_order_items_order")
    )
    @JsonBackReference
    private Order order;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(
            name = "product_id",
            nullable = false,
            foreignKey = @ForeignKey(name = "fk_order_items_product")
    )
    private Product product;

    @Min(1)
    @Column(nullable = false)
    @Builder.Default
    private int quantity = 1;

    /**
     * Snapshot mezők, hogy a rendelés utólag is konzisztens maradjon:
     * - unitPriceAtPurchase: egységár a rendelés pillanatában
     * Service rétegben töltjük, amikor az ordert mentjük.
     */
    @NotNull
    @Column(name = "unit_price_at_purchase", precision = 12, scale = 2, nullable = false)
    private BigDecimal unitPriceAtPurchase;


    public BigDecimal getLineTotal() {
        return unitPriceAtPurchase.multiply(BigDecimal.valueOf(quantity));
    }
}