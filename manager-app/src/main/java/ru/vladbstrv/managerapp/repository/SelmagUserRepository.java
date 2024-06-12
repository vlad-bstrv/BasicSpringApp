package ru.vladbstrv.managerapp.repository;

import org.springframework.data.repository.CrudRepository;
import ru.vladbstrv.managerapp.entity.SelmagUser;

import java.util.Optional;

public interface SelmagUserRepository extends CrudRepository<SelmagUser, Integer> {

    Optional<SelmagUser> findByUsername(String username);
}
