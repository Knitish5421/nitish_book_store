
package com.Bookstore.respository;

import java.sql.Date;
import java.util.stream.Stream;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.Bookstore.model.User;
import com.Bookstore.security.PasswordResetToken;

public interface PasswordResetTokenRespository extends JpaRepository<PasswordResetToken, Long>  {
	PasswordResetToken findByToken(String token);
	PasswordResetToken findByUser(User user);
	
	Stream<PasswordResetToken> findAllByExpiryDateLessThan(Date now);
	
	@Modifying
	@Query("delete from PasswordResetToken t where t.expiryDate <=?1")
	void deleteAllExpiredSince(Date now);

}
