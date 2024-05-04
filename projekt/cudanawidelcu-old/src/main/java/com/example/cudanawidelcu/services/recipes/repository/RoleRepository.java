package com.example.cudanawidelcu.services.recipes.repository;

import com.example.cudanawidelcu.services.recipes.model.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
}
