package br.com.ottimizza.application.domain.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DadosReceitaWS {

    public String nome;

    public String cnpj;

    public String telefone;

    public String email;

}