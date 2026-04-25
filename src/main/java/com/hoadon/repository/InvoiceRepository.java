package com.hoadon.repository;

import com.hoadon.entity.Invoice;
import com.hoadon.entity.InvoiceStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
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

    @Query(value =
           "SELECT i.* FROM invoices i LEFT JOIN customers c ON i.customer_id = c.id WHERE " +
           "(CAST(:status AS text) IS NULL OR i.status = CAST(:status AS text)) AND " +
           "(CAST(:search AS text) IS NULL OR LOWER(c.name) LIKE '%' || LOWER(CAST(:search AS text)) || '%' OR c.phone LIKE '%' || CAST(:search AS text) || '%') AND " +
           "(CAST(:fromDate AS text) IS NULL OR i.invoice_date >= CAST(:fromDate AS date)) AND " +
           "(CAST(:toDate AS text) IS NULL OR i.invoice_date <= CAST(:toDate AS date))",
           countQuery =
           "SELECT COUNT(i.id) FROM invoices i LEFT JOIN customers c ON i.customer_id = c.id WHERE " +
           "(CAST(:status AS text) IS NULL OR i.status = CAST(:status AS text)) AND " +
           "(CAST(:search AS text) IS NULL OR LOWER(c.name) LIKE '%' || LOWER(CAST(:search AS text)) || '%' OR c.phone LIKE '%' || CAST(:search AS text) || '%') AND " +
           "(CAST(:fromDate AS text) IS NULL OR i.invoice_date >= CAST(:fromDate AS date)) AND " +
           "(CAST(:toDate AS text) IS NULL OR i.invoice_date <= CAST(:toDate AS date))",
           nativeQuery = true)
    Page<Invoice> findWithFilters(
            @Param("status") String status,
            @Param("search") String search,
            @Param("fromDate") String fromDate,
            @Param("toDate") String toDate,
            Pageable pageable);

    @Query("SELECT i.invoiceNumber FROM Invoice i WHERE i.invoiceNumber IS NOT NULL")
    List<String> findAllInvoiceNumbers();

    @Modifying
    @Query("UPDATE Invoice i SET i.customer = NULL WHERE i.customer.id = :customerId")
    void detachCustomer(@Param("customerId") Long customerId);

    void deleteByCustomerId(Long customerId);
}
