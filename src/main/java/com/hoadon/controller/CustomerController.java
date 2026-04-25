package com.hoadon.controller;

import com.hoadon.dto.CustomerDTO;
import com.hoadon.dto.PagedResponse;
import com.hoadon.service.CustomerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/customers")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class CustomerController {

    private final CustomerService customerService;

    @GetMapping
    public ResponseEntity<PagedResponse<CustomerDTO>> getAllCustomers(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        limit = Math.min(limit, 100);
        return ResponseEntity.ok(customerService.getAllCustomers(search, page, limit));
    }

    @PostMapping
    public ResponseEntity<CustomerDTO> createCustomer(@RequestBody CustomerDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(customerService.createCustomer(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CustomerDTO> updateCustomer(
            @PathVariable Long id,
            @RequestBody CustomerDTO dto) {
        return ResponseEntity.ok(customerService.updateCustomer(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteCustomer(
            @PathVariable Long id,
            @RequestParam(defaultValue = "false") boolean deleteInvoices) {
        customerService.deleteCustomer(id, deleteInvoices);
        return ResponseEntity.noContent().build();
    }
}
