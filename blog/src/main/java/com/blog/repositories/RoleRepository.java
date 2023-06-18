package com.blog.repositories;

import com.blog.entities.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);//So this is how we can do 'Select * from table where columnName' condition. And we could also use & operator instead of or there, then that would become & Operator/
    //From here we need not remember any code nut remember just the Steps to build the code.
}
