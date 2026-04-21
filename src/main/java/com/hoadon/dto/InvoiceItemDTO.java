package com.hoadon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItemDTO {
    
    private Long id;
    private String productName;
    private String unit;
    private BigDecimal quantity;
    private BigDecimal unitPrice;
    private BigDecimal total;
}
