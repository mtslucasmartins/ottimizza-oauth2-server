package br.com.ottimizza.application.repositories.organizations;

import br.com.ottimizza.application.domain.dtos.OrganizationDTO;
import br.com.ottimizza.application.model.Organization;
import br.com.ottimizza.application.model.QOrganization;
import br.com.ottimizza.application.model.user.User;
import br.com.ottimizza.application.model.user_organization.QUserOrganization;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.querydsl.jpa.impl.JPAQuery;

@Repository
public class OrganizationRepositoryImpl implements OrganizationRepositoryCustom {

    @PersistenceContext
    EntityManager em;

    private QOrganization organization = QOrganization.organization1;

    private QUserOrganization userOrganization = QUserOrganization.userOrganization;

    @Override
    public Page<Organization> fetchAllByAccountantId(OrganizationDTO filter, Pageable pageable, User authorizedUser) {
        long totalElements = 0;
        JPAQuery<Organization> query = new JPAQuery<Organization>(em).from(organization);

        query.where(organization.organization.id.eq(authorizedUser.getOrganization().getId()));

        if (filter.getId() != null) {
            query.where(organization.id.eq(filter.getId()));
        }
        if (filter.getName() != null && !filter.getName().isEmpty()) {
            query.where(organization.name.like("%" + filter.getName() + "%"));
        }
        if (filter.getExternalId() != null && !filter.getExternalId().isEmpty()) {
            query.where(organization.externalId.like(filter.getExternalId()));
        }
        if (filter.getCnpj() != null && !filter.getCnpj().isEmpty()) {
            query.where(organization.cnpj.like(filter.getCnpj()));
        }
        if (filter.getCodigoERP() != null && !filter.getCodigoERP().isEmpty()) {
            query.where(organization.codigoERP.like(filter.getCodigoERP() + "%"));
        }

        totalElements = query.fetchCount();

        query.limit(pageable.getPageSize());
        query.offset(pageable.getPageSize() * pageable.getPageNumber());

        return new PageImpl<Organization>(query.fetch(), pageable, totalElements);
    }

    @Override // @formatter:off
    public Page<Organization> fetchAllByCustomerId(OrganizationDTO filter, Pageable pageable, User authorizedUser) {
        long totalElements = 0;
        JPAQuery<Organization> query = new JPAQuery<Organization>(em).from(organization)
            .innerJoin(userOrganization)
                .on(userOrganization.organization.id.eq(organization.id)
                .and(userOrganization.user.id.eq(authorizedUser.getId())));

        if (filter.getId() != null) {
            query.where(organization.id.eq(filter.getId()));
        }
        if (filter.getName() != null && !filter.getName().isEmpty()) {
            query.where(organization.name.like("%" + filter.getName() + "%"));
        }
        if (filter.getExternalId() != null && !filter.getExternalId().isEmpty()) {
            query.where(organization.externalId.like(filter.getExternalId()));
        }
        if (filter.getCnpj() != null && !filter.getCnpj().isEmpty()) {
            query.where(organization.cnpj.like(filter.getCnpj()));
        }
        if (filter.getCodigoERP() != null && !filter.getCodigoERP().isEmpty()) {
            query.where(organization.codigoERP.like(filter.getCodigoERP() + "%"));
        }

        totalElements = query.fetchCount();

        query.limit(pageable.getPageSize());
        query.offset(pageable.getPageSize() * pageable.getPageNumber());

        return new PageImpl<Organization>(query.fetch(), pageable, totalElements);
    } // @formatter:off


    /*
    @Override
	public KpiDTO findKpiDTOByCompanyId(BigInteger companyId) {
		
		JPAQuery<KpiDTO> query = new JPAQuery<KpiDTO>(em).from(kpi)
                .innerJoin(kpiDetail).on(kpiDetail.kpiID.id.eq(kpi.id))
                .where(kpi.company.id.eq(companyId)
                .and(kpi.graphType.in(7,12)));
        
        query.select(Projections.constructor(KpiDTO.class, kpi.title, kpiDetail.valorKPI));
		
        return query.fetchFirst();
	}    
    */

}
