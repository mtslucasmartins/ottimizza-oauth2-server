package br.com.ottimizza.application.domain.dtos;

import java.math.BigInteger;
import java.util.List;

import br.com.ottimizza.application.model.Authority;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserShortDTO {

	private BigInteger id;
	
	private String firstName;
	
	private String lastName;
	
	private String email;
	
	private String avatar;

	private List<String> products;
	
	private List<Authority> authorities;

	public UserShortDTO(BigInteger id, String firstName, String lastName, String email, String avatar) {
		super();
		this.id = id;
		this.firstName = firstName;
		this.lastName = lastName;
		this.email = email;
		this.avatar = avatar;
	}

	public UserShortDTO(BigInteger id) {
		super();
		this.id = id;
	}
	
	
	
}
