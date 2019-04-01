package ru.stnk.RestTestAPI.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.stnk.RestTestAPI.entity.Roles;

@Repository
public interface RolesRepository extends JpaRepository<Roles, Long> {

    Roles findByName(String name);

}
