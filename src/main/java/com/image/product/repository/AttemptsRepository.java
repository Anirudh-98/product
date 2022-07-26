package com.image.product.repository;

import com.image.product.model.Attempts;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface AttemptsRepository extends JpaRepository<Attempts, Integer> {

    Optional<Attempts> findAttemptsByUsername(String username);
}
