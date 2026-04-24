package com.hoadon.service;

import com.hoadon.dto.CustomerDTO;
import com.hoadon.dto.PagedResponse;
import com.hoadon.entity.Customer;
import com.hoadon.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;

    public PagedResponse<CustomerDTO> getAllCustomers(String search, int page, int limit) {
        Pageable pageable = PageRequest.of(page - 1, limit, Sort.by("id").ascending());
        Page<Customer> customers;
        if (search != null && !search.isBlank()) {
            customers = customerRepository.findByNameContainingIgnoreCaseOrPhoneContainingIgnoreCase(search, search, pageable);
        } else {
            customers = customerRepository.findAll(pageable);
        }
        Page<CustomerDTO> dtoPage = customers.map(this::toDTO);
        return PagedResponse.of(dtoPage, page, limit);
    }

    public CustomerDTO createCustomer(CustomerDTO dto) {
        Customer customer = new Customer();
        customer.setName(dto.getName());
        customer.setAddress(dto.getAddress());
        customer.setPhone(dto.getPhone());
        customer.setTaxCode(dto.getTaxCode());
        return toDTO(customerRepository.save(customer));
    }

    public CustomerDTO updateCustomer(Long id, CustomerDTO dto) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Customer not found"));
        customer.setName(dto.getName());
        customer.setAddress(dto.getAddress());
        customer.setPhone(dto.getPhone());
        customer.setTaxCode(dto.getTaxCode());
        return toDTO(customerRepository.save(customer));
    }

    public void deleteCustomer(Long id) {
        customerRepository.deleteById(id);
    }

    private CustomerDTO toDTO(Customer c) {
        return new CustomerDTO(c.getId(), c.getName(), c.getAddress(), c.getPhone(), c.getTaxCode());
    }
}
