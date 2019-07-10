package br.com.ottimizza.application.repositories.users;

import java.util.List;
import java.util.Optional;

import br.com.ottimizza.application.model.User;
import br.com.ottimizza.application.repositories.PasswordRecoveryRepository;
import br.com.ottimizza.application.model.PasswordResetToken;
import br.com.ottimizza.application.model.QUser;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.querydsl.jpa.impl.JPAQuery;  

import org.springframework.stereotype.Repository;

@Repository
//@formatter:off
public class UsersRepositoryImpl implements UsersRepositoryCustom {

    @PersistenceContext
    EntityManager em;

    @Inject
    PasswordRecoveryRepository passwordRecoveryRepository;

    private QUser user = QUser.user;

    @Override
    public List<User> findAll(Integer pageSize, Integer pageIndex) {
        //#region | findAll 
        JPAQuery<User> query = new JPAQuery<User>(em).from(user);
        if (pageSize != null && pageSize > 0) {
            query.limit(pageSize);
            if (pageIndex != null && pageIndex > 0) {
                query.offset(pageSize * pageIndex);
            }
        }
        return query.orderBy(user.email.asc()).fetch();
        //#endregion
    }

    // @Override
    // public List<User> findUsersByEmail(String email) {
    //     //#region | findUsersByEmail 
    //     JPAQuery<User> query = new JPAQuery<User>(em)
    //             .from(user)
    //             .where(user.email.like("%" + email + "%"));
    //     return query.orderBy(user.email.desc()).fetch();
    //     //#endregion
    // }

    // @Override
    // public List<User> findUsersByEmailPaginated(String email, Integer pageSize, Integer pageIndex) {
    //     //#region | findUsersByEmailPaginated 
    //     JPAQuery<User> query = new JPAQuery<User>(em)
    //             .from(user)
    //             .where(user.email.like("%" + email + "%"));
    //     // if (pageSize != null && pageSize > 0) {
    //         query.limit(pageSize);
    //         if (pageIndex != null && pageIndex > 0) {
    //             query.offset(pageSize * pageIndex);
    //         }
    //     // }
    //     return query.orderBy(user.email.asc()).fetch();
    //     //#endregion
    // }

}
//@formatter:on