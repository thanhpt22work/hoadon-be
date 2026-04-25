package com.hoadon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceDTO {

    private Long id;
    private String name;
    private BigDecimal importPrice;
    private BigDecimal salePrice;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
