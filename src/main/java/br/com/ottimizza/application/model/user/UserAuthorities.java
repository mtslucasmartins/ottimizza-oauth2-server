package br.com.ottimizza.application.model.user;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users_authorities")
@Data
@NoArgsConstructor
@AllArgsConstructor // @formatter:off
public class UserAuthorities {
	
	@Column(name="fk_users_id")
	private BigInteger userId;
	
	@Column(name="fk_authorities_id")
	private BigInteger authoritiesId;

}
