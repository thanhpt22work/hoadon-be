package com.hoadon.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceDTO {
    
    private Long id;
    private String invoiceNumber;
    private LocalDate invoiceDate;
    
    private String companyName;
    private String companyContact;
    private String companyAddress;
    private String companyPhone;
    private String companyEmail;
    private String companyBank;
    private String companyZalo;

    // Client (customer)
    private Long customerId;
    private String clientName;
    private String clientAddress;
    private String clientPhone;
    private String clientTaxCode;
    
    private List<InvoiceItemDTO> items;
    
    private BigDecimal total;
    private BigDecimal paidAmount;
    private BigDecimal remaining;
    private String status;
    
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
