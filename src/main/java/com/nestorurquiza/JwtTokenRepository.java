package com.nestorurquiza;

import java.util.Date;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;
import java.time.Instant;

public interface JwtTokenRepository extends JpaRepository<JwtToken, Long> {
    Optional<JwtToken> findByToken(String token);

    @Transactional
    @Modifying
    @Query("DELETE FROM JwtToken jt WHERE jt.expiry < ?1")
    void deleteAllExpiredTokens(Instant now);
}
