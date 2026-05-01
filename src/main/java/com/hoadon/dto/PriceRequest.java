package com.hoadon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PriceRequest {
    private String name;
    private BigDecimal importPrice;
    private BigDecimal salePrice;
    private String note;
    private String imageUrl;
}
