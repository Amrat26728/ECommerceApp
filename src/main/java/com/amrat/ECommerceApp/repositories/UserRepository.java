package com.amrat.ECommerceApp.repositories;

import com.amrat.ECommerceApp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Long> {
}
