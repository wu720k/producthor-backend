package com.producthor.Producthor.repository;

import com.producthor.Producthor.domain.model.Category;
import com.producthor.Producthor.domain.model.Product;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class ProductRepositoryTest {

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void save_and_find_product() {
        Category c = Category.builder().name("Cat1").description("desc").build();
        c = categoryRepository.save(c);

        Product p = Product.builder()
                .name("P1")
                .description("D1")
                .price(new BigDecimal("1999"))
                .available(true)
                .imageUrl("/files/x.png")
                .category(c)
                .build();

        p = productRepository.save(p);

        var found = productRepository.findById(p.getId()).orElseThrow();
        assertThat(found.getName()).isEqualTo("P1");
        assertThat(found.getCategory().getId()).isEqualTo(c.getId());
    }

    @Test
    void findAll_returns_list() {
        var sizeBefore = productRepository.findAll().size();

        Product p = Product.builder().name("X").price(new BigDecimal("1")).available(true).build();
        productRepository.save(p);

        assertThat(productRepository.findAll().size()).isEqualTo(sizeBefore + 1);
    }
}
