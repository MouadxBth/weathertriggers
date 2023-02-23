package me.hanane.registration.token;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;

import java.util.Optional;

public interface ConfirmationTokenRepository extends
        JpaRepository<ConfirmationToken, Long>,
        JpaSpecificationExecutor<ConfirmationToken> {

    Optional<ConfirmationToken> findByToken(String token);
}
