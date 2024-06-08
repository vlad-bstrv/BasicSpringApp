package ru.vladbstrv.catalogueservice.repository;


import org.springframework.data.repository.CrudRepository;
import ru.vladbstrv.catalogueservice.entity.Product;

import java.util.List;
import java.util.Optional;

public interface ProductRepository extends CrudRepository<Product, Integer> {

}
