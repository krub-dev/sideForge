package com.sideforge.service.interfaces;

import com.sideforge.dto.customer.*;
import org.springframework.data.domain.*;

import java.util.List;

public interface CustomerService {
    // Create a new customer user from a CustomerRequestDTO.
    CustomerResponseDTO createCustomer(CustomerRequestDTO customerRequestDTO);

    // Retrieve a customer user by its unique identifier.
    CustomerResponseDTO getCustomerById(Long id);

    // Retrieve all customer users as a list (not paginated).
    List<CustomerResponseDTO> getAllCustomers();

    // Update an existing customer user by its unique identifier with the provided data.
    CustomerResponseDTO updateCustomer(Long id, CustomerUpdateDTO customerUpdateDTO);

    // Delete a customer user by its unique identifier.
    void deleteCustomer(Long id);

    // Retrieve all customer users as a paginated list.
    Page<CustomerResponseDTO> getCustomersPage(Pageable pageable);
}
