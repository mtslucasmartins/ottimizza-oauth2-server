package br.com.ottimizza.application.repositories;

import br.com.ottimizza.application.model.PasswordResetToken;

import java.math.BigInteger;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface PasswordRecoveryRepository extends JpaRepository<PasswordResetToken, BigInteger> {

    @Query("SELECT prt FROM PasswordResetToken prt WHERE LOWER(prt.token) = LOWER(:token)")
    PasswordResetToken findByToken(@Param("token") String token);

}

// public String validatePasswordResetToken(long id, String token) {
//     PasswordResetToken passToken =
//       passwordTokenRepository.findByToken(token);
//     if ((passToken == null) || (passToken.getUser()
//         .getId() != id)) {
//         return "invalidToken";
//     }
//  
//     Calendar cal = Calendar.getInstance();
//     if ((passToken.getExpiryDate()
//         .getTime() - cal.getTime()
//         .getTime()) <= 0) {
//         return "expired";
//     }
//  
//     User user = passToken.getUser();
//     Authentication auth = new UsernamePasswordAuthenticationToken(
//       user, null, Arrays.asList(
//       new SimpleGrantedAuthority("CHANGE_PASSWORD_PRIVILEGE")));
//     SecurityContextHolder.getContext().setAuthentication(auth);
//     return null;
// }