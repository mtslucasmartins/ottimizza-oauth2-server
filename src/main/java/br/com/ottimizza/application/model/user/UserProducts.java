package br.com.ottimizza.application.model.user;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users_products")
@Data
@NoArgsConstructor
@AllArgsConstructor // @formatter:off
public class UserProducts {
	
	@Column(name="fk_users_id")
	private BigInteger usersId;
	
	@Column(name="fk_products_id")
	private BigInteger productsId;
}
