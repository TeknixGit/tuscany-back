package com.tuscany.tour.repository;

import com.tuscany.tour.models.AppRole;
import com.tuscany.tour.models.Role;
import com.tuscany.tour.models.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByRoleName(AppRole appRole);

}