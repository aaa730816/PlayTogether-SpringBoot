package com.shu.tony.PlayTogether.nonentity.common;

import lombok.Data;

import java.util.List;
@Data
public class PageResult<T> {
    private int page;
    private int size;
    private int totalPages;
    private List<T> content;

    public PageResult(int page, int size,int totalPages, List<T> content) {
        this.page = page;
        this.size = size;
        this.totalPages = totalPages;
        this.content = content;
    }

    public PageResult() {
    }
}
