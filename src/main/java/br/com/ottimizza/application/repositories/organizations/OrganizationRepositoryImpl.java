package br.com.ottimizza.application.repositories.organizations;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import br.com.ottimizza.application.model.Organization;
import br.com.ottimizza.application.model.User;
import br.com.ottimizza.application.model.QOrganization;
import br.com.ottimizza.application.model.QUser;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.querydsl.jpa.impl.JPAQuery;

import org.springframework.stereotype.Repository;

@Repository //@formatter:off
public class OrganizationRepositoryImpl implements OrganizationRepositoryCustom {

    @PersistenceContext
    EntityManager em;

    private QOrganization organization = QOrganization.organization1;
    private QUser user = QUser.user;

    @Override
    public List<Organization> findAll(String filter, Integer pageIndex, Integer pageSize) {
        //#region | findAll 
        JPAQuery<Organization> query = new JPAQuery<Organization>(em).from(organization);
        if (filter != null && !filter.equals("")) {
            query.where(organization.name.like("%" + filter + "%"));
        }
        query.orderBy(organization.name.asc());
        if (pageSize != null && pageSize > 0) {
            query.limit(pageSize);
            if (pageIndex != null && pageIndex > 0) {
                query.offset(pageSize * pageIndex);
            }
        }
        return query.fetch();
        //#endregion
    }

    @Override
    public List<Organization> findAllByAccountingId(String filter, Integer pageIndex, Integer pageSize, BigInteger accoutingId) {
        //#region | findAll 
        JPAQuery<Organization> query = new JPAQuery<Organization>(em).from(organization);
        query.where(organization.organization.id.eq(accoutingId));
        if (filter != null && !filter.equals("")) {
            query.where(organization.name.like("%" + filter + "%"));
        }
        query.orderBy(organization.name.asc());
        if (pageSize != null && pageSize > 0) {
            query.limit(pageSize);
            if (pageIndex != null && pageIndex > 0) {
                query.offset(pageSize * pageIndex);
            }
        }
        return query.fetch();
        //#endregion
    }

    @Override
    public List<Organization> findAllByAccountingIdAndUsername(
        String filter, Integer pageIndex, Integer pageSize, BigInteger accoutingId, String username) {
        JPAQuery<Organization> query = new JPAQuery<Organization>(em).from(user)
            .innerJoin(user.organizations, organization) // joins with organizations 
            .where(
                user.username.eq(username), 
                organization.organization.id.eq(accoutingId))
            .select(organization); // selects only the organizations

        if (filter != null && !filter.equals("")) {
            query.where(organization.name.like("%" + filter + "%"));
        }
        query.orderBy(organization.name.asc());
        if (pageSize != null && pageSize > 0) {
            query.limit(pageSize);
            if (pageIndex != null && pageIndex > 0) {
                query.offset(pageSize * pageIndex);
            }
        }
        return query.fetch();
        //#endregion
    }
}
//@formatter:on