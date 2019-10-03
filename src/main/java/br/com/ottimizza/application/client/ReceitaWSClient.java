package br.com.ottimizza.application.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import br.com.ottimizza.application.domain.dtos.DadosReceitaWS;

@FeignClient(name = "ReceitaWebService", url = "https://www.receitaws.com.br")
public interface ReceitaWSClient {

    @GetMapping("/v1/cnpj/{cnpj}")
    public HttpEntity<DadosReceitaWS> getInfo(@PathVariable String cnpj);

}