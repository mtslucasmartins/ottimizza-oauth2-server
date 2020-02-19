package br.com.ottimizza.application.repositories.users;

import br.com.ottimizza.application.domain.dtos.UserDTO;
import br.com.ottimizza.application.model.user.User;
import br.com.ottimizza.application.model.user_organization.QUserOrganization;
import br.com.ottimizza.application.model.user_organization.UserOrganization;
import br.com.ottimizza.application.utils.QueryDSLUtils;
import br.com.ottimizza.application.model.user.QUser;

import java.util.ArrayList;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import com.querydsl.core.types.Order;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;
import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQuery;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Repository;

@Repository
public class UsersRepositoryImpl implements UsersRepositoryCustom {

    @PersistenceContext
    EntityManager em;

    private static final String QUSER_NAME = "user";

    private QUser user = QUser.user;

    private QUserOrganization userOrganization = QUserOrganization.userOrganization;

    @Override
    public Page<User> fetchAll(UserDTO filter, Pageable pageable) {
        long totalElements = 0;
        JPAQuery<User> query = new JPAQuery<User>(em).from(user);

        totalElements = filter(query, filter);  
        sort(query, pageable, User.class, QUSER_NAME);
        paginate(query, pageable);
        
        return new PageImpl<User>(query.fetch(), pageable, totalElements);
    }

    @Override
    public Page<User> fetchAll(UserDTO filter, Pageable pageable, User authorizedUser) {
        long totalElements = 0;
        JPAQuery<User> query = new JPAQuery<User>(em).from(user);
        
        totalElements = filter(query, filter);  
        sort(query, pageable, User.class, QUSER_NAME);
        paginate(query, pageable);
        
        return new PageImpl<User>(query.fetch(), pageable, totalElements);
    }

    @Override
    public Page<User> fetchAllCustomers(UserDTO filter, Pageable pageable, User authorizedUser) {
        long totalElements = 0;
        JPAQuery<User> query = new JPAQuery<User>(em).from(user);

        // query.where(user.organization.eq(authorizedUser.getOrganization()));
        query.where(user.id.in(JPAExpressions.select(userOrganization.user.id)
                                  .from(userOrganization)
                                  .where(userOrganization.organization.id.in(
                                      JPAExpressions.select(userOrganization.organization.id)
                                                    .from(userOrganization)
                                                    .where(userOrganization.user.id.eq(authorizedUser.getId()))
                                  ))));

        totalElements = filter(query, filter);  
        sort(query, pageable, User.class, QUSER_NAME);
        paginate(query, pageable);
        
        return new PageImpl<User>(query.fetch(), pageable, totalElements);
    }

    private <T> long filter(JPAQuery<T> query, UserDTO filter) {
        if (filter != null){
            if (filter.getId() != null) {
                query.where(user.id.eq(filter.getId()));
            }
            if (filter.getFirstName() != null && !filter.getFirstName().isEmpty()) {
                query.where(QueryDSLUtils.unnacent(user.firstName, "%" + filter.getFirstName() + "%"));
            }
            if (filter.getLastName() != null && !filter.getLastName().isEmpty()) {
                query.where(QueryDSLUtils.unnacent(user.lastName, "%" + filter.getLastName() + "%"));
            }
            if (filter.getEmail() != null && !filter.getEmail().isEmpty()) {
                query.where(QueryDSLUtils.unnacent(user.lastName, "%" + filter.getEmail() + "%"));
            }
            if (filter.getUsername() != null && !filter.getUsername().isEmpty()) {
                query.where(QueryDSLUtils.unnacent(user.username, "%" + filter.getUsername() + "%"));
            }
            if (filter.getActive() != null) {
                query.where(user.active.eq(filter.getActive()));
            }
            if (filter.getType() != null) {
                query.where(user.type.eq(filter.getType()));
            }
            if (filter.getOrganizationId() != null) {
                query.where(user.organization.id.eq(filter.getOrganizationId()));
            }
        }
        return query.fetchCount();
    }

    private <T> JPAQuery<T> paginate(JPAQuery<T> query, Pageable pageable) {
        query.limit(pageable.getPageSize());
        query.offset(pageable.getPageSize() * pageable.getPageNumber());
        return query;
    }

    private <T> JPAQuery<T> sort(JPAQuery<T> query, Pageable pageable, Class<T> clazz, String value) {
        PathBuilder<T> entityPath = new PathBuilder<T>(clazz, value);
        for (Sort.Order order : pageable.getSort()) {
            PathBuilder<Object> propertyPath = entityPath.get(order.getProperty());
            query.orderBy(new OrderSpecifier(Order.valueOf(order.getDirection().name()), propertyPath));
        }
        return query;
    }
}
