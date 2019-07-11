package br.com.ottimizza.application.model;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Column;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

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
    private BigInteger id;

    @Getter @Setter
    @Column(name = "externalId", nullable = false)
    private String externalId;

    @Getter @Setter
    @Column(name = "name", nullable = false)
    private String name;

    @Getter @Setter
    @Column(name = "cnpj", nullable = false)
    private String cnpj;

    @Getter @Setter
    @Column(name = "avatar", nullable = true)
    private String avatar;

}
