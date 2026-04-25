package com.hoadon.repository;

import com.hoadon.entity.Price;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PriceRepository extends JpaRepository<Price, Long> {

    Page<Price> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
