/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.hello.model.Product;

import br.edu.utfpr.hello.model.User.User;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;
import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class ProductRepositoryTest {

    private User user;
    private Product product;

    @Autowired
    ProductRepository productRepository;


    public ProductRepositoryTest() {
    }

    @Before
    public void setUp() {
        this.user = new User("User", "user@user.com", "123456");
        this.product = new Product(
                "produto",
                "descrição",
                "100",
                "R$ 100,00",
                "01/01/2020",
                "Unidade");

        product.setUser(user);
        productRepository.save(this.product);
    }

    @After
    public void tearDown() {
        productRepository.deleteAll();
    }

    @Test
    public void findAllByUserId() {

        List<Product> products = productRepository.findAllByUserId(this.user.getId());
        assertFalse(products.isEmpty());
    }

    @Test
    public void findAll() {
        List<Product> products = productRepository.findAll();
        assertFalse(products.isEmpty());
    }

    @Test
    public void findById() {
        Optional<Product> product = productRepository.findById(this.product.getId());
        assertNotNull(product.get());
    }

    @Test
    public void delete() {
        productRepository.delete(this.product);
        List<Product> products = productRepository.findAll();
        assertTrue(products.isEmpty());
    }
}
