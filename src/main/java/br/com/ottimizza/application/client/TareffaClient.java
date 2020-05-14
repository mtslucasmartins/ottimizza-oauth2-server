package br.com.ottimizza.application.client;

import br.com.ottimizza.application.model.tareffa.UsuarioTareffa;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;

@FeignClient(name = "${tareffa.service.name}", url = "${tareffa.service.url}")
public interface TareffaClient {
    
    @PostMapping("/services/usuarios/password")
    public ResponseEntity<String> updateUserPasswordTareffa(
        @RequestHeader("Authorization") String authorization,
        @RequestBody UsuarioTareffa usuario
    );
}