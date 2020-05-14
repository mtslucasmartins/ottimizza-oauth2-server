package br.com.ottimizza.application.model.tareffa;

import java.io.Serializable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UsuarioTareffa implements Serializable{
    
    private String email;
    private String senha;
}