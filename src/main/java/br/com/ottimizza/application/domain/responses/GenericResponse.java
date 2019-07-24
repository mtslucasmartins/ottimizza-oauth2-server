package br.com.ottimizza.application.domain.responses;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class GenericResponse<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    @JsonProperty("record")
    private T record;

}
