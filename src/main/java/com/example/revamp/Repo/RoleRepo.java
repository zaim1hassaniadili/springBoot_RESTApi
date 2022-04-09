package com.example.revamp.Repo;


import com.example.revamp.Model.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepo extends JpaRepository<Role, Long> {
    Role getRoleByName(String name);

}
