package br.com.ottimizza.application.repositories.users;

import java.math.BigInteger;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.ottimizza.application.domain.dtos.UserDTO;
import br.com.ottimizza.application.domain.dtos.UserShortDTO;
import br.com.ottimizza.application.model.user.User;

public interface UsersRepositoryCustom {
    // UsersRepositoryImpl
    Page<User> fetchAll(UserDTO filter, Pageable pageable);

    Page<User> fetchAll(UserDTO filter, Pageable pageable, User authorizedUser);

    Page<User> fetchAllCustomers(UserDTO filter, Pageable pageable, User authorizedUser);
    
    Page<UserShortDTO> fetchUserShort(UserDTO filter, Pageable pageable, BigInteger organizationId);
    
    List<BigInteger> fetchIds(UserDTO filter, BigInteger oganizationId); 

}
