/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package br.edu.utfpr.hello.model.User;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Optional;

import static org.junit.Assert.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@ActiveProfiles("test")
public class UserRepositoryTest {

    private User user;

    @Autowired
    UserRepository userRepository;

    public UserRepositoryTest() {

    }

    @Before
    public void setUp() {

        this.user = new User("User", "user@user.com", "123456");
        this.userRepository.save(this.user);
    }

    @After
    public void tearDown() {
        userRepository.deleteAll();
    }

    @Test
    public void findByEmailAndPassword() {
        Optional<User> user = userRepository.findByEmailAndPassword("user@user.com", "123456");
        assertEquals(this.user.getEmail(), user.get().getEmail());
    }

    @Test
    public void findByEmail() {
        Optional<User> user = userRepository.findByEmail("user@user.com");
        assertTrue(user.isPresent());
        assertEquals(this.user.getEmail(), user.get().getEmail());
    }

    @Test
    public void save() {
        User user = new User("novo", "novo@novo.com", "123456");
        userRepository.save(user);
        assertNotNull(userRepository.findByEmail(user.getEmail()));
    }
}
