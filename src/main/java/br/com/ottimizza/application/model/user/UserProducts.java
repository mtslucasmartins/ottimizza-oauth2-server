package br.com.ottimizza.application.model.user;

import java.math.BigInteger;

import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import br.com.ottimizza.application.model.Organization;
import br.com.ottimizza.application.model.product.Product;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "users_products")
@Data
@NoArgsConstructor
@AllArgsConstructor // @formatter:off
public class UserProducts {
	
	@EmbeddedId
	private UserProductsId id;
	
	@ManyToOne(targetEntity = User.class)
    @JoinColumn(name = "fk_users_id", insertable = false, updatable = false)
	private BigInteger usersId;
	
	@ManyToOne(targetEntity = Product.class)
    @JoinColumn(name = "fk_products_id", insertable = false, updatable = false)
	private BigInteger productsId;
}
