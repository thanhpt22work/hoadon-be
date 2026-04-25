package com.hoadon.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.math.BigDecimal;

@Entity
@Table(name = "invoice_items")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class InvoiceItem {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private String productName;
    
    @Column(nullable = false)
    private String unit;
    
    @Column(precision = 10, scale = 2)
    private BigDecimal length;

    @Column(precision = 10, scale = 2)
    private BigDecimal width;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal quantity;
    
    @Column(nullable = false, precision = 15, scale = 2)
    private BigDecimal unitPrice;
    
    @Column(precision = 15, scale = 2)
    private BigDecimal total;
    
    @PrePersist
    protected void onCreate() {
        if (quantity == null) {
            quantity = BigDecimal.ONE;
        }
        if (total == null) {
            total = quantity.multiply(unitPrice);
        }
    }
}
