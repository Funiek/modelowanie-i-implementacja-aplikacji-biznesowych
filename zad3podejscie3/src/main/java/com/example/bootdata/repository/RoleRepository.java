package com.example.bootdata.repository;

import com.example.bootdata.model.Role;
import com.example.bootdata.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role,Long> {
}
