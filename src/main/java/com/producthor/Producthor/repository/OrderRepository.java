package com.producthor.Producthor.repository;


import com.producthor.Producthor.domain.model.Order;
import com.producthor.Producthor.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface OrderRepository extends JpaRepository<Order, Long> {

    List<Order> findByUser(User user);

    @Modifying
    @Query("update Order o set o.user = null where o.user.id = :userId")
    void clearUserFromOrders(Long userId);
}
