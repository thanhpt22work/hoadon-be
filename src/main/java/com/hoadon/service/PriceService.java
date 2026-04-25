package com.hoadon.service;

import com.hoadon.dto.PagedResponse;
import com.hoadon.dto.PriceDTO;
import com.hoadon.entity.Price;
import com.hoadon.repository.PriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

@Service
@RequiredArgsConstructor
@Transactional
public class PriceService {

    private final PriceRepository priceRepository;

    public PagedResponse<PriceDTO> getAllPrices(String search, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("name").ascending());
        Page<Price> result = (search != null && !search.isBlank())
                ? priceRepository.findByNameContainingIgnoreCase(search, pageable)
                : priceRepository.findAll(pageable);
        return PagedResponse.of(result.map(this::toDTO), page, limit);
    }

    public PriceDTO createPrice(PriceDTO dto) {
        Price price = new Price();
        price.setName(dto.getName());
        price.setImportPrice(dto.getImportPrice());
        price.setSalePrice(dto.getSalePrice());
        return toDTO(priceRepository.save(price));
    }

    public PriceDTO updatePrice(Long id, PriceDTO dto) {
        Price price = priceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Price item not found"));
        price.setName(dto.getName());
        price.setImportPrice(dto.getImportPrice());
        price.setSalePrice(dto.getSalePrice());
        return toDTO(priceRepository.save(price));
    }

    public void deletePrice(Long id) {
        priceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Price item not found"));
        priceRepository.deleteById(id);
    }

    private PriceDTO toDTO(Price p) {
        return new PriceDTO(p.getId(), p.getName(), p.getImportPrice(), p.getSalePrice(),
                p.getCreatedAt(), p.getUpdatedAt());
    }
}
