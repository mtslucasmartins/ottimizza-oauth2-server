package br.com.ottimizza.application.domain.responses;

import java.io.Serializable;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.data.domain.Page;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class GenericPageableResponse<T> implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    @JsonProperty("records")
    private List<T> records;

    @Getter
    @Setter
    @JsonProperty("pageInfo")
    private PageInfoResponseObject pageInfo;

    public GenericPageableResponse(Page<T> page) {
        this.records = page.getContent();
        this.pageInfo = new PageInfoResponseObject(page);
    }

}
