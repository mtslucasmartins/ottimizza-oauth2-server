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
    @NonNull
    @Getter @Setter
    @Column(name = "id", nullable = false)
    @GeneratedValue(strategy = GenerationType.AUTO)
    private BigInteger id;

    @NonNull
    @Getter @Setter
    @Column(name = "external_id", nullable = false)
    private String externalId;

    @NonNull
    @Getter @Setter
    @Column(name = "name", nullable = false)
    private String name;

    @NonNull
    @Getter @Setter
    @Column(name = "type", nullable = true)
    private Integer type = OrganizationTypes.CLIENT.getValue();

    @NonNull
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
