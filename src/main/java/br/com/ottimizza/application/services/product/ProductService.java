package br.com.ottimizza.application.services.product;

import java.security.Principal;
import java.util.List;
import java.util.stream.Collectors;

import javax.inject.Inject;

import org.springframework.stereotype.Service;

import br.com.ottimizza.application.domain.dtos.ProductDTO;
import br.com.ottimizza.application.domain.mappers.ProductMapper;
import br.com.ottimizza.application.repositories.products.ProductRepository;

@Service
public class ProductService {

    @Inject
    private ProductRepository productRepository;

    public List<ProductDTO> findByGroup(String group, Principal principal) throws Exception {
        return productRepository.findAllByGroup(group)
                                .stream().map(ProductMapper::fromEntity).collect(Collectors.toList());
    } 

    public ProductDTO save(ProductDTO productDTO, Principal principal) throws Exception {
        return ProductMapper.fromEntity(productRepository.save(ProductMapper.fromDTO(productDTO));
    } 

}