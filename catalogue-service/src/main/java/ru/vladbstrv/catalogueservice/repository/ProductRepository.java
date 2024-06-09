package ru.vladbstrv.catalogueservice.repository;


import org.springframework.data.repository.CrudRepository;
import ru.vladbstrv.catalogueservice.entity.Product;


public interface ProductRepository extends CrudRepository<Product, Integer> {

    Iterable<Product> findAllByTitleLikeIgnoreCase(String filter);

}
