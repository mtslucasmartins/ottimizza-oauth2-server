package br.com.ottimizza.application.repositories.users;

import br.com.ottimizza.application.domain.dtos.UserDTO;
import br.com.ottimizza.application.model.user.User;
import br.com.ottimizza.application.model.user_organization.QUserOrganization;
import br.com.ottimizza.application.model.user_organization.UserOrganization;
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

    @SuppressWarnings("unused")
    private Page<User> fetchCustomers(UserDTO filter, Pageable pageable, User authenticated) {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<User> query = builder.createQuery(User.class);
        Root<User> from = query.from(User.class);

        Subquery<UserOrganization> usersSubquery = query.subquery(UserOrganization.class);
        Root<UserOrganization> fromUsers = usersSubquery.from(UserOrganization.class);
        // select fk_users_id from uses_organizations.
        usersSubquery.select(fromUsers.get("user").get("id")); 
        // Subquery to get all organizations by user id 
        Subquery<UserOrganization> organizationsSubquery = query.subquery(UserOrganization.class);
        Root<UserOrganization> fromOrganizations = organizationsSubquery.from(UserOrganization.class);
        // select fk_organizations_id from uses_organizations.
        organizationsSubquery.select(fromOrganizations.get("organization").get("id")); 
        organizationsSubquery.where(builder.equal(fromOrganizations.get("user").get("id"), authenticated.getId()));
        // get all users where organization id in subquery to get all organizations by authenticated id.
        usersSubquery.where(builder.in(fromUsers.get("organization").get("id")).value(organizationsSubquery));

        return new PageImpl<User>(new ArrayList<>(), pageable, 0);
    }

    private Expression<String> unaccent(CriteriaBuilder cb, Path<String> path) {
        return cb.function("unaccent", String.class, cb.upper(path));
    }


}
