package br.edu.utfpr.hello.services;

import br.edu.utfpr.hello.model.User.User;
import br.edu.utfpr.hello.model.User.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    @Autowired
    UserRepository userRepository;

    public List<User> all() {
        return userRepository.findAll();
    }

    public Optional<User> find(Long id) {
        return userRepository.findById(id);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }
}
