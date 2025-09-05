package com.producthor.Producthor.domain.mapper;

import com.producthor.Producthor.domain.dao.UserDao;
import com.producthor.Producthor.domain.dto.AuthenticationDto;
import com.producthor.Producthor.domain.model.User;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.stream.Collectors;

public final class UserMapper {

    private UserMapper() {}

    public static User fromAuth(AuthenticationDto dto, PasswordEncoder encoder, boolean isAdmin) {
        return User.builder()
                .username(dto.getUsername())
                .password(encoder.encode(dto.getPassword()))
                .name(null)
                .isAdmin(isAdmin)
                .build();
    }

    public static UserDao toDao(User user, boolean includeOrderHistory) {
        if (user == null) return null;

        return UserDao.builder()
                .id(user.getId())
                .username(user.getUsername())
                .name(user.getName())
                .shippingData(ShippingDataMapper.toDao(user.getShippingData()))
                .orderHistory(includeOrderHistory && user.getOrderHistory() != null
                        ? user.getOrderHistory().stream()
                        .map(OrderMapper::toDao)
                        .collect(Collectors.toList())
                        : null)
                .build();
    }
}
