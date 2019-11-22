package br.com.ottimizza.application.domain.dtos.responses;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class GenericResponse<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    @JsonProperty("record")
    private T record;

    @Getter
    @Setter
    @JsonProperty("records")
    private List<T> records;

    public GenericResponse(List<T> records) {
        this.records = records;
    }

    public GenericResponse(T record) {
        this.record = record;
    }

}
