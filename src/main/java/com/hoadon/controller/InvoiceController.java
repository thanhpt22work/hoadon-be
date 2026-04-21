package com.hoadon.controller;

import com.hoadon.dto.InvoiceDTO;
import com.hoadon.service.InvoiceService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/invoices")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class InvoiceController {
    
    private final InvoiceService invoiceService;
    
    @PostMapping
    public ResponseEntity<InvoiceDTO> createInvoice(@RequestBody InvoiceDTO invoiceDTO) {
        InvoiceDTO created = invoiceService.createInvoice(invoiceDTO);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }
    
    @GetMapping
    public ResponseEntity<Page<InvoiceDTO>> getAllInvoices(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String search,
            @RequestParam(required = false) String fromDate,
            @RequestParam(required = false) String toDate,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "20") int limit,
            @RequestParam(defaultValue = "createdAt") String sortBy,
            @RequestParam(defaultValue = "desc") String sortOrder
    ) {
        // TODO: Map params to service
        Pageable pageable = Pageable.unpaged(); // Cần chỉnh lại cho đúng phân trang
        Page<InvoiceDTO> invoices = invoiceService.getAllInvoicesWithFilter(status, search, fromDate, toDate, page, limit, sortBy, sortOrder, pageable);
        return ResponseEntity.ok(invoices);
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<InvoiceDTO> getInvoiceById(@PathVariable String id) {
        InvoiceDTO invoice = invoiceService.getInvoiceById(id);
        return ResponseEntity.ok(invoice);
    }
    
    @PatchMapping("/{id}")
    public ResponseEntity<InvoiceDTO> updateInvoice(
            @PathVariable String id,
            @RequestBody InvoiceDTO invoiceDTO) {
        InvoiceDTO updated = invoiceService.updateInvoice(id, invoiceDTO);
        return ResponseEntity.ok(updated);
    }
    
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteInvoice(@PathVariable String id) {
        invoiceService.deleteInvoice(id);
        return ResponseEntity.noContent().build();
    }
    

    @PatchMapping("/{id}/status")
    public ResponseEntity<InvoiceDTO> updateInvoiceStatus(
            @PathVariable String id,
            @RequestBody String status) {
        InvoiceDTO updated = invoiceService.updateInvoiceStatus(id, status);
        return ResponseEntity.ok(updated);
    }

    @PostMapping("/{id}/payments")
    public ResponseEntity<?> addPaymentToInvoice(
            @PathVariable String id,
            @RequestBody Object paymentRequest) {
        // TODO: Thay Object bằng DTO phù hợp
        Object result = invoiceService.addPaymentToInvoice(id, paymentRequest);
        return ResponseEntity.status(HttpStatus.CREATED).body(result);
    }
}
