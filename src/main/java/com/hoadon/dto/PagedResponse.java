package com.hoadon.dto;

import lombok.Data;
import org.springframework.data.domain.Page;

import java.util.List;

@Data
public class PagedResponse<T> {

    private List<T> data;
    private Meta meta;

    @Data
    public static class Meta {
        private int page;
        private int limit;
        private long totalItems;
        private int totalPages;
    }

    public static <T> PagedResponse<T> of(Page<T> page, int pageNumber, int limit) {
        PagedResponse<T> response = new PagedResponse<>();
        response.setData(page.getContent());

        Meta meta = new Meta();
        meta.setPage(pageNumber);
        meta.setLimit(limit);
        meta.setTotalItems(page.getTotalElements());
        meta.setTotalPages(page.getTotalPages());
        response.setMeta(meta);

        return response;
    }
}
