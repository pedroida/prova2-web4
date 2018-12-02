package br.edu.utfpr.hello.services;

import br.edu.utfpr.hello.model.Product.Product;
import br.edu.utfpr.hello.model.Product.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    ProductRepository productRepository;

    public void save(Product product) {
        productRepository.save(product);
    }

    public void delete(Long id) {
        productRepository.deleteById(id);
    }

    public List<Product> findAll() {
        return productRepository.findAll();
    }

    public Optional<Product> find(Long id) {
        return productRepository.findById(id);
    }

    public List<Product> findAllByUserId(Long id) {
        return productRepository.findAllByUserId(id);
    }
}
