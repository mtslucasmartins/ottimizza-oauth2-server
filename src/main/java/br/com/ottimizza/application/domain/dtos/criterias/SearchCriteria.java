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
public class SearchCriteria implements Serializable {

    private static final long serialVersionUID = 1L;

    private SortCritera sort;

}