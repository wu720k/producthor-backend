package com.producthor.Producthor.service;

import com.producthor.Producthor.domain.dao.UserDao;
import com.producthor.Producthor.domain.dto.AuthenticationDto;
import com.producthor.Producthor.domain.dto.UserDto;
import com.producthor.Producthor.domain.mapper.ShippingDataMapper;
import com.producthor.Producthor.domain.mapper.UserMapper;
import com.producthor.Producthor.domain.model.ShippingData;
import com.producthor.Producthor.domain.model.User;
import com.producthor.Producthor.repository.OrderRepository;
import com.producthor.Producthor.repository.UserRepository;
import com.producthor.Producthor.rest.response.BaseResponse;
import com.producthor.Producthor.rest.response.LoginResponse;
import com.producthor.Producthor.rest.response.UserResponse;
import com.producthor.Producthor.rest.response.UsersResponse;
import com.producthor.Producthor.service.authentication.JwtTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.Authentication;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final OrderRepository orderRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final UserDetailsService userDetailsService;
    private final JwtTokenService jwtTokenService;

    public BaseResponse registration(AuthenticationDto dto) {
        if (userRepository.existsByUsername(dto.getUsername())) {
            return new BaseResponse("ERROR", "Username already taken");
        }

        User user = UserMapper.fromAuth(dto, passwordEncoder, false);
        userRepository.save(user);
        return new BaseResponse("OK", "");
    }

    public LoginResponse login(AuthenticationDto dto) {
        try {
            var authToken = new UsernamePasswordAuthenticationToken(dto.getUsername(), dto.getPassword());
            authenticationManager.authenticate(authToken);
        } catch (AuthenticationException ex) {
            return LoginResponse.builder()
                    .token(null)
                    .username(null)
                    .isAdmin(false)
                    .baseResponse(new BaseResponse("ERROR", "Hibás bejelentkezési adatok"))
                    .build();
        }

        var userDetails = userDetailsService.loadUserByUsername(dto.getUsername());
        String token = jwtTokenService.generateToken(userDetails);

        boolean isAdmin = userRepository.findByUsername(dto.getUsername())
                .map(User::isAdmin)
                .orElse(false);

        return LoginResponse.builder()
                .token(token)
                .username(dto.getUsername())
                .isAdmin(isAdmin)
                .baseResponse(new BaseResponse("OK", ""))
                .build();
    }

    @Transactional
    public BaseResponse update(UserDto dto) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        var userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            return new BaseResponse("ERROR", "Authenticated user not found");
        }

        var user = userOpt.get();

        if (dto.getName() != null) {
            user.setName(dto.getName());
        }

        if (dto.getShippingData() != null) {
            ShippingData current = user.getShippingData();
            if (current == null) {
                ShippingData created = ShippingDataMapper.toEntity(dto.getShippingData());
                user.setShippingData(created);
            } else {
                ShippingDataMapper.updateEntityFromDto(dto.getShippingData(), current);
            }
        }

        userRepository.save(user);
        return new BaseResponse("OK", "");
    }

    @Transactional(readOnly = true)
    public UserResponse getCurrent() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()
                || auth.getPrincipal() == null
                || "anonymousUser".equals(auth.getPrincipal())) {
            return UserResponse.builder()
                    .user(null)
                    .baseResponse(new BaseResponse("ERROR", "Not authenticated"))
                    .build();
        }

        String username = auth.getName();
        var userOpt = userRepository.findByUsername(username);
        if (userOpt.isEmpty()) {
            return UserResponse.builder()
                    .user(null)
                    .baseResponse(new BaseResponse("ERROR", "User not found"))
                    .build();
        }

        User user = userOpt.get();

        UserDao dao = UserMapper.toDao(user, true);

        return UserResponse.builder()
                .user(dao)
                .baseResponse(new BaseResponse("OK", ""))
                .build();
    }

    @Transactional(readOnly = true)
    public UsersResponse getAll() {
        var users = userRepository.findAll().stream()
                .map(u -> UserMapper.toDao(u, false))
                .toList();

        return UsersResponse.builder()
                .users(users)
                .baseResponse(new BaseResponse("OK", ""))
                .build();
    }

    @Transactional
    public BaseResponse deleteById(Long userId) {
        if (!userRepository.existsById(userId)) {
            return new BaseResponse("ERROR", "User not found");
        }

        orderRepository.clearUserFromOrders(userId);
        userRepository.deleteById(userId);
        return new BaseResponse("OK", "");
    }

}
