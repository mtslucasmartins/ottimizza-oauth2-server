package br.com.ottimizza.application.repositories.users;

import br.com.ottimizza.application.domain.dtos.UserDTO;
import br.com.ottimizza.application.model.user.User;
import br.com.ottimizza.application.model.user_organization.QUserOrganization;
import br.com.ottimizza.application.model.user.QUser;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

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

    private QUser user = QUser.user;

    private QUserOrganization userOrganization = QUserOrganization.userOrganization;

    @Override
    public Page<User> fetchAll(UserDTO filter, Pageable pageable) {
        long totalElements = 0;
        JPAQuery<User> query = new JPAQuery<User>(em).from(user);
        if (filter.getId() != null) {
            query.where(user.id.eq(filter.getId()));
        }
        if (filter.getEmail() != null && !filter.getEmail().isEmpty()) {
            query.where(user.email.like("%" + filter.getEmail() + "%"));
        }
        if (filter.getUsername() != null && !filter.getUsername().isEmpty()) {
            query.where(user.email.like(filter.getUsername()));
        }
        if (filter.getType() != null && filter.getType() > 0) {
            query.where(user.type.eq(filter.getType()));
        }
        if (filter.getActive() != null) {
            query.where(user.active.eq(filter.getActive()));
        }

        PathBuilder<User> entityPath = new PathBuilder<>(User.class, "user");
        for (Sort.Order order : pageable.getSort()) {
            PathBuilder<Object> path = entityPath.get(order.getProperty());
            query.orderBy(new OrderSpecifier(Order.valueOf(order.getDirection().name()), path));
        }

        totalElements = query.fetchCount();
        query.limit(pageable.getPageSize());
        query.offset(pageable.getPageSize() * pageable.getPageNumber());

        return new PageImpl<User>(query.fetch(), pageable, totalElements);
    }

    @Override
    public Page<User> fetchAll(UserDTO filter, Pageable pageable, User authorizedUser) {
        long totalElements = 0;
        JPAQuery<User> query = new JPAQuery<User>(em).from(user);
        if (filter.getId() != null) {
            query.where(user.id.eq(filter.getId()));
        }
        if (filter.getEmail() != null && !filter.getEmail().isEmpty()) {
            query.where(user.email.like("%" + filter.getEmail() + "%"));
        }
        if (filter.getUsername() != null && !filter.getUsername().isEmpty()) {
            query.where(user.email.like(filter.getUsername()));
        }
        if (filter.getType() != null && filter.getType() > 0) {
            query.where(user.type.eq(filter.getType()));
        }
        if (filter.getActive() != null) {
            query.where(user.active.eq(filter.getActive()));
        }

        query.where(user.organization.eq(authorizedUser.getOrganization()));

        PathBuilder<User> entityPath = new PathBuilder<>(User.class, "user");
        for (Sort.Order order : pageable.getSort()) {
            PathBuilder<Object> path = entityPath.get(order.getProperty());
            query.orderBy(new OrderSpecifier(Order.valueOf(order.getDirection().name()), path));
        }

        totalElements = query.fetchCount();
        query.limit(pageable.getPageSize());
        query.offset(pageable.getPageSize() * pageable.getPageNumber());
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

        if (filter.getId() != null) {
            query.where(user.id.eq(filter.getId()));
        }
        if (filter.getEmail() != null && !filter.getEmail().isEmpty()) {
            query.where(user.email.like("%" + filter.getEmail() + "%"));
        }
        if (filter.getUsername() != null && !filter.getUsername().isEmpty()) {
            query.where(user.email.like(filter.getUsername()));
        }
        if (filter.getType() != null && filter.getType() > 0) {
            query.where(user.type.eq(filter.getType()));
        }
        if (filter.getActive() != null) {
            query.where(user.active.eq(filter.getActive()));
        }


        PathBuilder<User> entityPath = new PathBuilder<>(User.class, "user");
        for (Sort.Order order : pageable.getSort()) {
            PathBuilder<Object> path = entityPath.get(order.getProperty());
            query.orderBy(new OrderSpecifier(Order.valueOf(order.getDirection().name()), path));
        }

        totalElements = query.fetchCount();
        query.limit(pageable.getPageSize());
        query.offset(pageable.getPageSize() * pageable.getPageNumber());
        return new PageImpl<User>(query.fetch(), pageable, totalElements);
    }

}
