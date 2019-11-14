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

    @JsonProperty("page_index")
    public Integer pageIndex = 0;

    @JsonProperty("page_size")
    public Integer pageSize = 10;

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