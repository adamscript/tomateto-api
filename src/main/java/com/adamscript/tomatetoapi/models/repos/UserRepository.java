package com.adamscript.tomatetoapi.models.repos;

import com.adamscript.tomatetoapi.models.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
