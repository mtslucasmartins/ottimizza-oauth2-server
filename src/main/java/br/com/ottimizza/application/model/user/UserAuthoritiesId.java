package br.com.ottimizza.application.model.user;

import java.io.Serializable;
import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Embeddable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Embeddable
public class UserAuthoritiesId  implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Column(name="fk_users_id")
	private BigInteger usersId;
	
	@Column(name="fk_authorities_id")
	private String authoritiesId;
}
