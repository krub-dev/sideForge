package com.sideforge.controller;

import com.sideforge.dto.customer.CustomerRequestDTO;
import com.sideforge.dto.customer.CustomerResponseDTO;
import com.sideforge.dto.customer.CustomerUpdateDTO;
import com.sideforge.dto.user.*;
import com.sideforge.service.interfaces.CustomerService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.*;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/customers")
@Validated
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @GetMapping
    @Operation(summary = "Get all customers", description = "Returns a list of all customers.")
    public ResponseEntity<List<CustomerResponseDTO>> getAllCustomers() {
        return ResponseEntity.ok(customerService.getAllCustomers());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get customer by ID", description = "Returns the details of a customer by their ID.")
    public ResponseEntity<CustomerResponseDTO> getCustomerById(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(customerService.getCustomerById(id));
    }

    @PostMapping
    @Operation(summary = "Create customer", description = "Creates a new customer and returns it.")
    public ResponseEntity<CustomerResponseDTO> createCustomer(@Valid @RequestBody CustomerRequestDTO body) {
        CustomerResponseDTO created = customerService.createCustomer(body);
        URI location = URI.create("/api/customers/" + created.getId());
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update customer", description = "Updates the data of an existing customer.")
    public ResponseEntity<CustomerResponseDTO> updateCustomer(@PathVariable @Positive Long id, @Valid @RequestBody CustomerUpdateDTO body) {
        return ResponseEntity.ok(customerService.updateCustomer(id, body));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete customer", description = "Deletes a customer by their ID.")
    public ResponseEntity<Void> deleteCustomer(@PathVariable @Positive Long id) {
        customerService.deleteCustomer(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/page")
    @Operation(summary = "Get paginated customers", description = "Returns a paginated list of customers.")
    public ResponseEntity<Page<CustomerResponseDTO>> getCustomersPage(
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @PositiveOrZero int size,
            @RequestParam(defaultValue = "id,asc") String[] sort
    ) {
        Sort sortObj = Sort.by(Sort.Direction.fromString(sort[1]), sort[0]);
        Pageable pageable = PageRequest.of(page, size, sortObj);
        return ResponseEntity.ok(customerService.getCustomersPage(pageable));
    }
}
