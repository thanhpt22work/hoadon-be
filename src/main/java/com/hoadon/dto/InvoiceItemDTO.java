package com.hoadon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class InvoiceItemDTO {
    
    private Long id;
    private String productName;
    private String unit;
    private BigDecimal length;
    private BigDecimal width;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal total;
}
