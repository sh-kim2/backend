package com.scalablescripts.auth.data;

import org.springframework.data.jdbc.repository.query.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.Optional;

@Repository
public interface UserRepo extends CrudRepository<User, Long> {

    // select * from user u where u.email =: email
    Optional<User> findByEmail(String email);  // Optional 선택사항을 반영, NullPointerException 방지, 초기값을 선정

    @Query("""
        SELECT u.id, u.first_name, u.last_name, u.email, u.password FROM user_tbl u INNER JOIN token t ON u.id = t.user_tbl
        WHERE u.id = :id AND t.refresh_token = :refreshToken AND t.expired_at >= :expiredAt
    """)
    Optional<User> findByIdAndTokensRefreshTokenAndTokensExpiredAtGreaterThan(Long id, String refreshToken, LocalDateTime expiredAt);

    @Query("""
        select u.id, u.first_name, u.last_name, u.email, u.password from user_tbl u inner join password_recovery pr on u.id = pr.user_tbl
        where pr.token = :token
    """
    )
    Optional<User> findByPasswordRecoveriesToken(String token);
}
