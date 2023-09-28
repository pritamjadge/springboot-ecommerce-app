package com.project.springbootangularcrud.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class PaginationPageResponse<T> {
    private List<T> content;
    private int currentPage;
    private long totalItems;
    private int totalPages;

    public PaginationPageResponse(List<T> content, int currentPage, long totalItems, int totalPages) {
        this.content = content;
        this.currentPage = currentPage;
        this.totalItems = totalItems;
        this.totalPages = totalPages;
    }
}
