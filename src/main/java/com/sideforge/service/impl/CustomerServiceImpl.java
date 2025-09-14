package com.sideforge.service.impl;

import com.sideforge.dto.customer.*;
import com.sideforge.enums.Role;
import com.sideforge.exception.ResourceNotFoundException;
import com.sideforge.model.Customer;
import com.sideforge.repository.CustomerRepository;
import com.sideforge.service.interfaces.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    // Create a new customer from the provided DTO.
    @Override
    @Transactional
    public CustomerResponseDTO createCustomer(CustomerRequestDTO customerRequestDTO) {
        Customer customer = Customer.builder()
                .username(customerRequestDTO.getUsername())
                .email(customerRequestDTO.getEmail())
                // WARNING: Saving plain text password. In production, always hash the password!
                .passwordHash(customerRequestDTO.getPassword())
                .role(customerRequestDTO.getRole() != null ? customerRequestDTO.getRole() : Role.CUSTOMER)
                .profileImageUrl(customerRequestDTO.getProfileImageUrl())
                .preferredLanguage(customerRequestDTO.getPreferredLanguage())
                .isVerified(customerRequestDTO.getIsVerified())
                .build();

        Customer saved = customerRepository.save(customer);
        return toResponseDTO(saved);
    }

    // Retrieve a customer by their unique ID.
    @Override
    public CustomerResponseDTO getCustomerById(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        return toResponseDTO(customer);
    }

    // Get a list with all customers.
    @Override
    public List<CustomerResponseDTO> getAllCustomers() {
        return customerRepository.findAll()
                .stream()
                .map(CustomerServiceImpl::toResponseDTO)
                .collect(Collectors.toList());
    }

    // Update a customer by their unique ID with the provided data.
    @Override
    @Transactional
    public CustomerResponseDTO updateCustomer(Long id, CustomerUpdateDTO customerUpdateDTO) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        customer.setUsername(customerUpdateDTO.getUsername());
        customer.setEmail(customerUpdateDTO.getEmail());
        // Only update password if provided (empty or null = ignore)
        if (customerUpdateDTO.getPassword() != null && !customerUpdateDTO.getPassword().isBlank()) {
            // Password is being saved in plain text.
            // Change when Security is implemented!
            customer.setPasswordHash(customerUpdateDTO.getPassword());
        }
        customer.setRole(customerUpdateDTO.getRole());
        customer.setProfileImageUrl(customerUpdateDTO.getProfileImageUrl());
        customer.setPreferredLanguage(customerUpdateDTO.getPreferredLanguage());
        customer.setIsVerified(customerUpdateDTO.getIsVerified());
        // lastLogin should be managed by login logic, not update here

        Customer saved = customerRepository.save(customer);
        return toResponseDTO(saved);
    }

    // Delete a customer by their unique ID
    @Override
    @Transactional
    public void deleteCustomer(Long id) {
        Customer customer = customerRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + id));
        customerRepository.delete(customer);
    }

    // Get a paginated list of customers
    @Override
    public Page<CustomerResponseDTO> getCustomersPage(Pageable pageable) {
        return customerRepository.findAll(pageable)
                .map(CustomerServiceImpl::toResponseDTO);
    }

    // Helper method to map Customer to CustomerResponseDTO
    private static CustomerResponseDTO toResponseDTO(Customer customer) {
        if (customer == null) {
            return null;
        }
        return CustomerResponseDTO.builder()
                .id(customer.getId())
                .username(customer.getUsername())
                .email(customer.getEmail())
                .role(customer.getRole())
                .profileImageUrl(customer.getProfileImageUrl())
                .preferredLanguage(customer.getPreferredLanguage())
                .isVerified(customer.getIsVerified())
                .build();
    }
}
