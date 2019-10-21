package br.com.ottimizza.application.domain.dtos;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class DadosReceitaWS implements Serializable {

    /**
     *
     */
    private static final long serialVersionUID = 1L;

    public String nome;

    public String cnpj;

    public String telefone;

    public String email;

}