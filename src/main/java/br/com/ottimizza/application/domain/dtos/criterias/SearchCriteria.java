package br.com.ottimizza.application.domain.dtos.criterias;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

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

    @JsonProperty("sort")
    private SortCriteria sort = new SortCriteria();

    @JsonProperty(value = "page_index", defaultValue = "0")
    public Integer pageIndex;

    @JsonProperty(value = "page_size", defaultValue = "10")
    public Integer pageSize;

    @Setter
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @EqualsAndHashCode
    public class SortCriteria implements Serializable {

        private static final long serialVersionUID = 1L;

        public String order;

        public String attribute;

        public List<String> attributes;

    }

}