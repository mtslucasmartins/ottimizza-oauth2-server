package br.com.ottimizza.application.domain.dtos.criterias;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class SortCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private Sort sort;

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    class Sort implements Serializable {
        public String order;

        public String attribute;

        public String attributes;

    }

}