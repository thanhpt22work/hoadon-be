package com.hoadon.repository;

import com.hoadon.entity.Invoice;
import com.hoadon.entity.InvoiceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long> {
    
    Optional<Invoice> findByInvoiceNumber(String invoiceNumber);
    
    Page<Invoice> findByStatus(InvoiceStatus status, Pageable pageable);
    
    Page<Invoice> findByCustomer_NameContainingIgnoreCase(String customerName, Pageable pageable);
    
    Page<Invoice> findByInvoiceDateBetween(LocalDate startDate, LocalDate endDate, Pageable pageable);
    
    List<Invoice> findByStatus(InvoiceStatus status);
    
    @Query("SELECT i FROM Invoice i WHERE i.status = :status")
    Page<Invoice> findByStatusCustom(@Param("status") InvoiceStatus status, Pageable pageable);

    @Query("SELECT i.invoiceNumber FROM Invoice i WHERE i.invoiceNumber IS NOT NULL")
    List<String> findAllInvoiceNumbers();
}
