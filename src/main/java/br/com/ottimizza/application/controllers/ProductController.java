package br.com.ottimizza.application.controllers;

import java.math.BigInteger;
import java.security.Principal;

import javax.inject.Inject;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.ottimizza.application.domain.dtos.ProductDTO;
import br.com.ottimizza.application.domain.dtos.responses.GenericResponse;
import br.com.ottimizza.application.model.user.User;
import br.com.ottimizza.application.services.UserService;
import br.com.ottimizza.application.services.product.ProductService;

@RestController
@RequestMapping("/api/v1/products")
public class ProductController {

    @Inject
    ProductService productService;
    
    @Inject
    UserService userService;

    @GetMapping
    public ResponseEntity<?> findAllByGroup(@RequestParam("group") String group, Principal principal) throws Exception {
    	User authorizedUser = userService.findByUsername(principal.getName());
        return ResponseEntity.ok(new GenericResponse<ProductDTO>(productService.findByGroup(group, principal, authorizedUser.getId())));
    }

    @PostMapping
    public ResponseEntity<?> save(@RequestBody ProductDTO productDTO, Principal principal) throws Exception {
        return ResponseEntity.ok(new GenericResponse<ProductDTO>(productService.save(productDTO, principal)));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<?> save(@PathVariable("id") BigInteger id, @RequestBody ProductDTO productDTO, Principal principal) throws Exception {
        return ResponseEntity.ok(new GenericResponse<ProductDTO>(productService.patch(id, productDTO, principal)));
    }
    
}
