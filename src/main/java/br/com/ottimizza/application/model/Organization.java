package br.com.ottimizza.application.model;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Column;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.SequenceGenerator;
import javax.persistence.Table;

import br.com.ottimizza.application.domain.OrganizationTypes;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.NonNull;
import lombok.Setter;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "organizations") // @formatter:off
public class Organization implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Getter @Setter
    @Column(name = "id", nullable = false)
    @SequenceGenerator(name = "organizations_sequence", sequenceName = "organizations_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "organizations_sequence")
    private BigInteger id;

    @Getter @Setter
    @Column(name = "external_id", nullable = false)
    private String externalId;

    @Getter @Setter
    @Column(name = "name", nullable = false)
    private String name;

    @Getter @Setter
    @Column(name = "type", nullable = true)
    private Integer type = OrganizationTypes.CLIENT.getValue();

    @Getter @Setter
    @Column(name = "cnpj", nullable = false)
    private String cnpj;

    @Getter @Setter
    @Column(name = "codigo_erp", nullable = true)
    private String codigoERP;

    @Getter @Setter
    @Column(name = "avatar")
    private String avatar;

    @Getter @Setter
    @ManyToOne
    @JoinColumn(name = "fk_organizations_id")
    private Organization organization;

}
