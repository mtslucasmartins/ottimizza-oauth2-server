package br.com.ottimizza.application.repositories.organizations;

import br.com.ottimizza.application.domain.dtos.OrganizationDTO;
import br.com.ottimizza.application.model.Organization;
import br.com.ottimizza.application.model.QOrganization;
import br.com.ottimizza.application.model.user.User;
import br.com.ottimizza.application.model.user_organization.QUserOrganization;
import br.com.ottimizza.application.model.user_organization.UserOrganization;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import java.lang.reflect.Field;
import java.math.BigInteger;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Join;
import javax.persistence.criteria.Path;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.persistence.criteria.Subquery;

import com.querydsl.jpa.impl.JPAQuery;
import com.querydsl.core.BooleanBuilder;
// Sort
import com.querydsl.core.types.Order;
import org.springframework.data.domain.Sort;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.dsl.PathBuilder;

@Repository // @formatter:off
public class OrganizationRepositoryImpl implements OrganizationRepositoryCustom {

    private final String QORGANIZATION_NAME = "organization1";

    private long totalElements;

    private QOrganization organization = QOrganization.organization1;

    private QUserOrganization userOrganization = QUserOrganization.userOrganization;

    @PersistenceContext
    EntityManager em;

    @Override
    public Page<Organization> fetchAllByAccountantId(OrganizationDTO filter, Pageable pageable, User authorizedUser) {
        JPAQuery<Organization> query = new JPAQuery<Organization>(em).from(organization);
        BooleanBuilder builder = new BooleanBuilder();
        filter.setOrganizationId(authorizedUser.getOrganization().getId());
        // query.where(builder
        //     .or(organization.id.eq(filter.getId()))
        //    .or(organization.organization.id.eq(filter.getOrganizationId())));
        totalElements = filter(query, filter);
        sort(query, pageable, Organization.class, QORGANIZATION_NAME);
        paginate(query, pageable);
        return new PageImpl<Organization>(query.fetch(), pageable, totalElements);
    }

    @Override 
    public Page<Organization> fetchAllByCustomerId(BigInteger id, OrganizationDTO filter, Pageable pageable) {
        JPAQuery<Organization> query = new JPAQuery<Organization>(em).from(organization)
            .innerJoin(userOrganization)
                .on(userOrganization.organization.id.eq(organization.id)
                .and(userOrganization.user.id.eq(id)));
        totalElements = filter(query, filter);  
        sort(query, pageable, Organization.class, QORGANIZATION_NAME);
        paginate(query, pageable);
        return new PageImpl<Organization>(query.fetch(), pageable, totalElements);
    } 

    @Override 
    public Page<Organization> fetchAllByCustomerId(OrganizationDTO filter, Pageable pageable, User authorizedUser) {
        JPAQuery<Organization> query = new JPAQuery<Organization>(em).from(organization)
            .innerJoin(userOrganization)
                .on(userOrganization.organization.id.eq(organization.id)
                .and(userOrganization.user.id.eq(authorizedUser.getId())));
        totalElements = filter(query, filter);  
        sort(query, pageable, Organization.class, QORGANIZATION_NAME);
        paginate(query, pageable);
        return new PageImpl<Organization>(query.fetch(), pageable, totalElements);
    } 
    public Long countOrganizationsByCustomerId(OrganizationDTO filter, Pageable pageable, User authenticated) throws Exception {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Long> query = builder.createQuery(Long.class);
        Root<Organization> from = query.from(Organization.class);
        List<Predicate> predicateList = new ArrayList<Predicate>();

        // Subquery...
        Subquery<UserOrganization> organizationsSubquery = query.subquery(UserOrganization.class);
        Root<UserOrganization> fromOrganizations = organizationsSubquery.from(UserOrganization.class);
        organizationsSubquery.select(fromOrganizations.get("organization").get("id")); 
        organizationsSubquery.where(builder.equal(fromOrganizations.get("user").get("id"), authenticated.getId())); 
    
        Predicate predicate = builder.in(from.get("id")).value(organizationsSubquery);
        predicateList.add(predicate);
        predicateList.addAll(predicates(filter, builder, from));

        Predicate[] predicates = new Predicate[predicateList.size()];
        predicateList.toArray(predicates);
        query.select(builder.count(from));
        query.where(predicates);

        TypedQuery<Long> typedQuery = em.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        return em.createQuery(query).getSingleResult();
    }
    
    public Page<Organization> fetchOrganizationsByCustomerId(OrganizationDTO filter, Pageable pageable, User authenticated) 
            throws Exception {
        CriteriaBuilder builder = em.getCriteriaBuilder();
        CriteriaQuery<Organization> query = builder.createQuery(Organization.class);
        Root<Organization> from = query.from(Organization.class);
        List<Predicate> predicateList = new ArrayList<Predicate>();

        // Subquery...
        Subquery<UserOrganization> organizationsSubquery = query.subquery(UserOrganization.class);
        Root<UserOrganization> fromOrganizations = organizationsSubquery.from(UserOrganization.class);
        organizationsSubquery.select(fromOrganizations.get("organization").get("id")); 
        organizationsSubquery.where(builder.equal(fromOrganizations.get("user").get("id"), authenticated.getId())); 
    
        Predicate predicate = builder.in(from.get("id")).value(organizationsSubquery);

        predicateList.add(predicate);
        // predicateList.addAll();
        predicates(filter, builder, from);
        
        Predicate[] predicates = new Predicate[predicateList.size()];
        predicateList.toArray(predicates);
        query.where(predicates);

        TypedQuery<Organization> typedQuery = em.createQuery(query);
        typedQuery.setFirstResult((int) pageable.getOffset());
        typedQuery.setMaxResults(pageable.getPageSize());

        return new PageImpl<>(
            typedQuery.getResultList(), pageable, 
            countOrganizationsByCustomerId(filter, pageable, authenticated)
        );
    }

    public List<Predicate> predicates(OrganizationDTO filter, CriteriaBuilder cb, Root<Organization> root) 
            throws Exception { // @formatter:off
        final List<Predicate> predicates = new ArrayList<Predicate>();
        for (Field field : filter.getClass().getDeclaredFields()) {
            String name = field.getName();
            Object value = field.get(filter);
            if (value != null) {
                if (value instanceof String) {
                    Path<String> path = root.get(name);
                    predicates.add(cb.like(unaccent(cb, path), MessageFormat.format("%{0}%", (String) value).toUpperCase()));
                } else {
                    Path path = root.get(name);
                    predicates.add(cb.equal(path, value));
                }
            }
        }
        return predicates;
    }

    // @SuppressWarnings("unused")
    // private Page<User> fetchCustomers(UserDTO filter, Pageable pageable, User authenticated) {
    //     CriteriaBuilder builder = em.getCriteriaBuilder();
    //     CriteriaQuery<User> query = builder.createQuery(User.class);
    //     Root<User> from = query.from(User.class);

    //     Subquery<UserOrganization> usersSubquery = query.subquery(UserOrganization.class);
    //     Root<UserOrganization> fromUsers = usersSubquery.from(UserOrganization.class);
    //     // select fk_users_id from uses_organizations.
    //     usersSubquery.select(fromUsers.get("user").get("id")); 
    //     // Subquery to get all organizations by user id 
    //     Subquery<UserOrganization> organizationsSubquery = query.subquery(UserOrganization.class);
    //     Root<UserOrganization> fromOrganizations = organizationsSubquery.from(UserOrganization.class);
    //     // select fk_organizations_id from uses_organizations.
    //     organizationsSubquery.select(fromOrganizations.get("organization").get("id")); 
    //     organizationsSubquery.where(builder.equal(fromOrganizations.get("user").get("id"), authenticated.getId()));
    //     // get all users where organization id in subquery to get all organizations by authenticated id.
    //     usersSubquery.where(builder.in(fromUsers.get("organization").get("id")).value(organizationsSubquery));

    //     return new PageImpl<User>(new ArrayList<>(), pageable, 0);
    // }



    @Override
    public Page<Organization> fetchAll(OrganizationDTO filter, Pageable pageable, User authenticated) {
        JPAQuery<Organization> query = new JPAQuery<Organization>(em).from(organization)
            .leftJoin(organization.organization)
                .on(organization.organization.id.eq(organization.id));
        totalElements = filter(query, filter);  
        sort(query, pageable, Organization.class, QORGANIZATION_NAME);
        paginate(query, pageable);
        return new PageImpl<Organization>(query.fetch(), pageable, totalElements);
    }

    private <T> long filter(JPAQuery<T> query, OrganizationDTO filter) {
        if (filter != null){
            if (filter.getId() != null) {
                query.where(organization.id.eq(filter.getId()));
            }
            if (filter.getExternalId() != null && !filter.getExternalId().isEmpty()) {
                query.where(organization.externalId.like(filter.getExternalId()));
            }
            if (filter.getName() != null && !filter.getName().isEmpty()) {
                query.where(organization.name.like("%" + filter.getName() + "%"));
            }
            if (filter.getCnpj() != null && !filter.getCnpj().isEmpty()) {
                query.where(organization.cnpj.like(filter.getCnpj()));
            }
            if (filter.getActive() != null) {
                query.where(organization.active.eq(filter.getActive()));
            }
            if (filter.getCodigoERP() != null && !filter.getCodigoERP().isEmpty()) {
                query.where(organization.codigoERP.like(filter.getCodigoERP() + "%"));
            }
            if (filter.getType() != null) {
                query.where(organization.type.eq(filter.getType()));
            }
            if (filter.getOrganizationId() != null) {
                query.where(organization.organization.id.eq(filter.getOrganizationId()));
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

    private Expression<String> unaccent(CriteriaBuilder cb, Path<String> path) {
        return cb.function("unaccent", String.class, cb.upper(path));
    }
    
}
