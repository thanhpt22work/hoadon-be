package com.hoadon.controller;

import com.hoadon.dto.PagedResponse;
import com.hoadon.dto.PriceDTO;
import com.hoadon.service.PriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/prices")
@RequiredArgsConstructor
@CrossOrigin(origins = {"http://localhost:5173", "http://localhost:3000"})
public class PriceController {

    private final PriceService priceService;

    @GetMapping
    public ResponseEntity<PagedResponse<PriceDTO>> getAllPrices(
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "1") int page,
            @RequestParam(defaultValue = "10") int limit) {
        limit = Math.min(limit, 100);
        return ResponseEntity.ok(priceService.getAllPrices(search, page, limit));
    }

    @PostMapping
    public ResponseEntity<PriceDTO> createPrice(@RequestBody PriceDTO dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(priceService.createPrice(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PriceDTO> updatePrice(
            @PathVariable Long id,
            @RequestBody PriceDTO dto) {
        return ResponseEntity.ok(priceService.updatePrice(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrice(@PathVariable Long id) {
        priceService.deletePrice(id);
        return ResponseEntity.noContent().build();
    }
}
