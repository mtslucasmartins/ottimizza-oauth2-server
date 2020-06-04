package br.com.ottimizza.application.domain.dtos;

import java.math.BigInteger;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductShortDTO {

	private BigInteger id;
	
	private String name;
	
	
}


