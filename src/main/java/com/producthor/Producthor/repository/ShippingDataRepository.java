package com.producthor.Producthor.repository;

import com.producthor.Producthor.domain.model.ShippingData;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShippingDataRepository extends JpaRepository<ShippingData, Long> {
}