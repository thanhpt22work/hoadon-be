package com.hoadon.service;

import com.hoadon.dto.CustomerDTO;
import com.hoadon.entity.Customer;
import com.hoadon.repository.CustomerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional
public class CustomerService {

    private final CustomerRepository customerRepository;

    public List<CustomerDTO> getAllCustomers(String search) {
        List<Customer> customers;
        if (search != null && !search.isBlank()) {
            customers = customerRepository.findByNameContainingIgnoreCaseOrPhoneContaining(search, search);
        } else {
            customers = customerRepository.findAll();
        }
        return customers.stream().map(this::toDTO).collect(Collectors.toList());
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
