package br.edu.utfpr.hello.model.Product;

import br.edu.utfpr.hello.model.User.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    List<Product> findAllByUserId(Long id);
}
