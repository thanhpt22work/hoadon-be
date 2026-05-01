package com.hoadon.controller;

import com.hoadon.dto.PagedResponse;
import com.hoadon.dto.PriceDTO;
import com.hoadon.dto.PriceRequest;
import com.hoadon.service.PriceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.math.BigDecimal;

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

    @PostMapping(consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PriceDTO> createPrice(@RequestBody PriceRequest request) {
        return ResponseEntity.status(HttpStatus.CREATED).body(priceService.createPrice(request, null));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PriceDTO> createPriceMultipart(
            @RequestParam String name,
            @RequestParam(required = false) BigDecimal importPrice,
            @RequestParam(required = false) BigDecimal salePrice,
            @RequestParam(required = false) String note,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        PriceRequest request = new PriceRequest(name, importPrice, salePrice, note, null);
        return ResponseEntity.status(HttpStatus.CREATED).body(priceService.createPrice(request, image));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<PriceDTO> updatePrice(
            @PathVariable Long id,
            @RequestBody PriceRequest request) {
        return ResponseEntity.ok(priceService.updatePrice(id, request, null));
    }

    @PutMapping(value = "/{id}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PriceDTO> updatePriceMultipart(
            @PathVariable Long id,
            @RequestParam String name,
            @RequestParam(required = false) BigDecimal importPrice,
            @RequestParam(required = false) BigDecimal salePrice,
            @RequestParam(required = false) String note,
            @RequestPart(value = "image", required = false) MultipartFile image) {
        PriceRequest request = new PriceRequest(name, importPrice, salePrice, note, null);
        return ResponseEntity.ok(priceService.updatePrice(id, request, image));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletePrice(@PathVariable Long id) {
        priceService.deletePrice(id);
        return ResponseEntity.noContent().build();
    }
}

