package com.hoadon.repository;

import com.hoadon.entity.Customer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {

    Page<Customer> findByNameContainingIgnoreCaseOrPhoneContainingIgnoreCase(String name, String phone, Pageable pageable);

    Optional<Customer> findFirstByPhone(String phone);

    boolean existsByPhone(String phone);

    boolean existsByPhoneAndIdNot(String phone, Long id);
}
