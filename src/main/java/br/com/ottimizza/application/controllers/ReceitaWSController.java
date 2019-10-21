package br.com.ottimizza.application.controllers;

import java.security.Principal;

import javax.inject.Inject;

import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.ottimizza.application.client.ReceitaWSClient;
import br.com.ottimizza.application.domain.dtos.DadosReceitaWS;

@RestController
@RequestMapping("/api/v1/receitaws")
public class ReceitaWSController {

    @Inject
    ReceitaWSClient receitaWSClient;

    @GetMapping("/cnpj/{cnpj}")
    public HttpEntity<DadosReceitaWS> fetchById(@PathVariable String cnpj, Principal principal) throws Exception {
        System.out.println("CNPJ: " + cnpj);
        try {
            DadosReceitaWS data = receitaWSClient.getInfo(cnpj).getBody();

            System.out.println(data.getCnpj());
            // return ;
        } catch (Exception ex) {
            ex.printStackTrace();
        }

        return ResponseEntity.ok(new DadosReceitaWS());
    }

}