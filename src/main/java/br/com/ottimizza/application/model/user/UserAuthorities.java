package br.com.ottimizza.application.model.user;

import java.math.BigInteger;


import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.ottimizza.application.model.Authority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "users_authorities")
@NoArgsConstructor
@AllArgsConstructor // @formatter:off
public class UserAuthorities {
	
	@EmbeddedId
	private UserAuthoritiesId id;
	
	@Getter @Setter
	@ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "fk_users_id", insertable = false, updatable = false)
	private BigInteger usersId;

	@Getter @Setter
	@ManyToOne(targetEntity = Authority.class)
    @JoinColumn(name = "fk_authorities_id", insertable = false, updatable = false)
	private String authoritiesId;

}
