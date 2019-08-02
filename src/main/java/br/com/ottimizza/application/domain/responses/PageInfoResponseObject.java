package br.com.ottimizza.application.domain.responses;

import java.io.Serializable;
import com.fasterxml.jackson.annotation.JsonProperty;

import org.springframework.data.domain.Page;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
public class PageInfoResponseObject implements Serializable {

    private static final long serialVersionUID = 1L;

    @Getter
    @Setter
    @JsonProperty("hasNext")
    private boolean hasNext;

    @Getter
    @Setter
    @JsonProperty("hasPrevious")
    private boolean hasPrevious;

    @Getter
    @Setter
    @JsonProperty("pageSize")
    private int pageSize;

    @Getter
    @Setter
    @JsonProperty("pageIndex")
    private int pageIndex;

    @Getter
    @Setter
    @JsonProperty("totalPages")
    private int totalPages;

    @Getter
    @Setter
    @JsonProperty("totalElements")
    private long totalElements;

    public PageInfoResponseObject(Page<?> page) {
        this.hasNext = page.hasNext();
        this.hasPrevious = page.hasPrevious();

        this.pageSize = page.getSize();
        this.pageIndex = page.getNumber();

        this.totalPages = page.getTotalPages();
        this.totalElements = page.getTotalElements();
    }

}
