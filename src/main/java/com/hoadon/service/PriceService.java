
package com.hoadon.service;

import com.hoadon.dto.PagedResponse;
import com.hoadon.dto.PriceDTO;
import com.hoadon.dto.PriceRequest;
import com.hoadon.entity.Price;
import com.hoadon.repository.PriceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.server.ResponseStatusException;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Set;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class PriceService {

    private final PriceRepository priceRepository;

    @Value("${app.upload.dir:uploads/prices}")
    private String uploadDir;

    @Value("${app.base-url:http://localhost:8080/api}")
    private String baseUrl;

    private static final Set<String> ALLOWED_EXTENSIONS = Set.of("jpg", "jpeg", "jfif", "png", "webp");
    private static final long MAX_FILE_SIZE = 5 * 1024 * 1024; // 5MB

    public PagedResponse<PriceDTO> getAllPrices(String search, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("name").ascending());
        Page<Price> result = (search != null && !search.isBlank())
                ? priceRepository.findByNameContainingIgnoreCase(search, pageable)
                : priceRepository.findAll(pageable);
        return PagedResponse.of(result.map(this::toDTO), page, limit);
    }

    public PriceDTO createPrice(PriceRequest request, MultipartFile image) {
        Price price = new Price();
        applyRequest(price, request);
        if (image != null && !image.isEmpty()) {
            price.setImageUrl(saveImage(image));
        }
        return toDTO(priceRepository.save(price));
    }
    /**
     * Cập nhật thông tin price item.
     * - Nếu có ảnh mới: xóa ảnh cũ, lưu ảnh mới
     * - Nếu không có ảnh mới và imageUrl = null: xóa ảnh cũ
     * - Nếu không có ảnh mới và imageUrl có giá trị: giữ nguyên ảnh cũ
     */
    public PriceDTO updatePrice(Long id, PriceRequest request, MultipartFile image) {
        Price price = priceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Price item not found"));
        applyRequest(price, request);
        if (image != null && !image.isEmpty()) {
            deleteImageFile(price.getImageUrl());
            price.setImageUrl(saveImage(image));
        } else {
            // null = xóa ảnh, URL cũ = giữ nguyên
            if (request.getImageUrl() == null && price.getImageUrl() != null) {
                deleteImageFile(price.getImageUrl());
            }
            price.setImageUrl(request.getImageUrl());
        }
        return toDTO(priceRepository.save(price));
    }

    public void deletePrice(Long id) {
        Price price = priceRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Price item not found"));
        deleteImageFile(price.getImageUrl());
        priceRepository.deleteById(id);
    }

    private void applyRequest(Price price, PriceRequest request) {
        price.setName(request.getName());
        price.setImportPrice(request.getImportPrice());
        price.setSalePrice(request.getSalePrice());
        price.setNote(request.getNote());
        // imageUrl xử lý riêng theo từng case (update/create)
    }

    private void deleteImageFile(String imageUrl) {
        if (imageUrl == null) return;
        try {
            // Lấy filename từ URL: http://localhost:8080/api/uploads/prices/{filename}
            String filename = imageUrl.substring(imageUrl.lastIndexOf('/') + 1);
            Path filePath = Paths.get(uploadDir).toAbsolutePath().resolve(filename);
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            // Log lỗi nhưng không throw, không critical
        }
    }

    private String saveImage(MultipartFile image) {
        if (image.getSize() > MAX_FILE_SIZE) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Image file exceeds 5MB limit");
        }
        String ext = getExtension(image.getOriginalFilename());
        if (ext == null || !ALLOWED_EXTENSIONS.contains(ext.toLowerCase())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Only JPG, PNG, WEBP images are allowed");
        }
        String normalizedExt = ext.toLowerCase().equals("jfif") ? "jpg" : ext.toLowerCase();
        String filename = UUID.randomUUID() + "." + normalizedExt;
        try {
            Path uploadPath = Paths.get(uploadDir).toAbsolutePath();
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            Path targetFile = uploadPath.resolve(filename);
            Files.copy(image.getInputStream(), targetFile);
        } catch (IOException e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Failed to save image: " + e.getMessage());
        }
        return baseUrl + "/uploads/prices/" + filename;
    }

    private String getExtension(String filename) {
        if (filename == null) return null;
        int dot = filename.lastIndexOf('.');
        return dot >= 0 ? filename.substring(dot + 1) : null;
    }

    private PriceDTO toDTO(Price p) {
        PriceDTO dto = new PriceDTO();
        dto.setId(p.getId());
        dto.setName(p.getName());
        dto.setImportPrice(p.getImportPrice());
        dto.setSalePrice(p.getSalePrice());
        dto.setNote(p.getNote());
        dto.setImageUrl(p.getImageUrl());
        dto.setCreatedAt(p.getCreatedAt());
        dto.setUpdatedAt(p.getUpdatedAt());
        return dto;
    }
}
