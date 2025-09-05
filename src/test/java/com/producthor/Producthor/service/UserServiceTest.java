package com.producthor.Producthor.service;

import com.producthor.Producthor.domain.dto.ShippingDataDto;
import com.producthor.Producthor.domain.dto.UserDto;
import com.producthor.Producthor.domain.model.ShippingData;
import com.producthor.Producthor.domain.model.User;
import com.producthor.Producthor.repository.UserRepository;
import com.producthor.Producthor.rest.response.BaseResponse;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.test.context.ActiveProfiles;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test")
class UserServiceTest {

    @Autowired private UserService userService;
    @Autowired private UserRepository userRepository;

    private User savedUser;

    private void authenticate(String username) {
        SecurityContextHolder.getContext().setAuthentication(
                new UsernamePasswordAuthenticationToken(username, "pw")
        );
    }

    @BeforeEach
    void setUp() {
        userRepository.deleteAll();
        savedUser = userRepository.save(User.builder()
                .username("john")
                .name("John D")
                .build());
    }

    @AfterEach
    void tearDown() {
        SecurityContextHolder.clearContext();
    }

    @Test
    void update_creates_shipping_when_absent() {
        authenticate("john");

        UserDto dto = new UserDto();
        dto.setName("John Doe");
        dto.setShippingData(new ShippingDataDto("4400","Nyíregyháza","Kakas","5.",""));

        BaseResponse res = userService.update(dto);
        assertThat(res.getCode()).isEqualTo("OK");

        User u = userRepository.findById(savedUser.getId()).orElseThrow();
        assertThat(u.getName()).isEqualTo("John Doe");
        assertThat(u.getShippingData()).isNotNull();
        assertThat(u.getShippingData().getCity()).isEqualTo("Nyíregyháza");
    }

    @Test
    void update_modifies_existing_shipping() {
        ShippingData sd = ShippingData.builder()
                .postalCode("1111").city("Bp").street("Fő").houseNumber("1").build();
        savedUser.setShippingData(sd);
        userRepository.save(savedUser);

        authenticate("john");

        UserDto dto = new UserDto();
        dto.setName("John The Second");
        dto.setShippingData(new ShippingDataDto("4400","Nyíregyháza","Kakas","5.",""));

        BaseResponse res = userService.update(dto);
        assertThat(res.getCode()).isEqualTo("OK");

        User u = userRepository.findById(savedUser.getId()).orElseThrow();
        assertThat(u.getName()).isEqualTo("John The Second");
        assertThat(u.getShippingData().getPostalCode()).isEqualTo("4400");
        assertThat(u.getShippingData().getStreet()).isEqualTo("Kakas");
    }

    @Test
    void update_returns_error_if_user_not_authenticated() {
        SecurityContextHolder.clearContext(); // nincs auth

        UserDto dto = new UserDto();
        dto.setName("X");

        BaseResponse res = userService.update(dto);
        assertThat(res.getCode()).isEqualTo("ERROR");
        assertThat(res.getMessage()).containsIgnoringCase("Authenticated user not found");
    }
}
