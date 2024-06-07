package ru.vladbstrv.basicspringapp.repository;

import ru.vladbstrv.basicspringapp.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository {
    List<Product> findALl();

    Product save(Product product);

    Optional<Product> findById(Integer productId);

    void deleteById(Integer id);
}
