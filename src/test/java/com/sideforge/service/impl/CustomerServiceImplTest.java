package com.sideforge.service.impl;

import com.sideforge.dto.customer.*;
import com.sideforge.enums.PreferredLanguage;
import com.sideforge.enums.Role;
import com.sideforge.exception.ResourceNotFoundException;
import com.sideforge.model.Customer;
import com.sideforge.repository.CustomerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;
import org.springframework.data.domain.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CustomerServiceImplTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerServiceImpl customerService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void createCustomer_success() {
        CustomerRequestDTO dto = CustomerRequestDTO.builder()
                .username("newuser")
                .email("new@mail.com")
                .password("password123")
                .role(Role.CUSTOMER)
                .profileImageUrl("img.jpg")
                .preferredLanguage(PreferredLanguage.EN)
                .isVerified(true)
                .build();

        Customer customer = Customer.builder()
                .id(1L)
                .username("newuser")
                .email("new@mail.com")
                .passwordHash("password123")
                .role(Role.CUSTOMER)
                .profileImageUrl("img.jpg")
                .preferredLanguage(PreferredLanguage.EN)
                .isVerified(true)
                .build();

        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerResponseDTO response = customerService.createCustomer(dto);

        assertEquals("newuser", response.getUsername());
        assertEquals("new@mail.com", response.getEmail());
        assertEquals(Role.CUSTOMER, response.getRole());
        assertEquals("img.jpg", response.getProfileImageUrl());
        assertEquals(PreferredLanguage.EN, response.getPreferredLanguage());
        assertTrue(response.getIsVerified());
    }

    @Test
    void getCustomerById_found() {
        Customer customer = Customer.builder()
                .id(1L)
                .username("customer")
                .email("customer@mail.com")
                .role(Role.CUSTOMER)
                .build();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));

        CustomerResponseDTO dto = customerService.getCustomerById(1L);

        assertEquals(1L, dto.getId());
        assertEquals("customer", dto.getUsername());
        assertEquals("customer@mail.com", dto.getEmail());
        assertEquals(Role.CUSTOMER, dto.getRole());
    }

    @Test
    void getCustomerById_notFound() {
        when(customerRepository.findById(2L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> customerService.getCustomerById(2L));
    }

    @Test
    void getAllCustomers_success() {
        List<Customer> customers = Arrays.asList(
                Customer.builder().id(1L).username("a").email("a@mail.com").role(Role.CUSTOMER).build(),
                Customer.builder().id(2L).username("b").email("b@mail.com").role(Role.CUSTOMER).build()
        );
        when(customerRepository.findAll()).thenReturn(customers);

        List<CustomerResponseDTO> result = customerService.getAllCustomers();

        assertEquals(2, result.size());
        assertEquals("a", result.get(0).getUsername());
        assertEquals("b", result.get(1).getUsername());
    }

    @Test
    void updateCustomer_success() {
        CustomerUpdateDTO dto = CustomerUpdateDTO.builder()
                .username("updated")
                .email("updated@mail.com")
                .password("newpass")
                .role(Role.CUSTOMER)
                .profileImageUrl("img2.jpg")
                .preferredLanguage(PreferredLanguage.ES)
                .isVerified(false)
                .build();

        Customer customer = Customer.builder()
                .id(1L)
                .username("old")
                .email("old@mail.com")
                .passwordHash("oldpass")
                .role(Role.CUSTOMER)
                .profileImageUrl("img.jpg")
                .preferredLanguage(PreferredLanguage.EN)
                .isVerified(true)
                .build();

        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(customerRepository.save(any(Customer.class))).thenReturn(customer);

        CustomerResponseDTO response = customerService.updateCustomer(1L, dto);

        assertEquals("updated", response.getUsername());
        assertEquals("updated@mail.com", response.getEmail());
        assertEquals("img2.jpg", response.getProfileImageUrl());
        assertEquals(PreferredLanguage.ES, response.getPreferredLanguage());
        assertFalse(response.getIsVerified());
    }

    @Test
    void updateCustomer_notFound() {
        CustomerUpdateDTO dto = CustomerUpdateDTO.builder().build();
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> customerService.updateCustomer(1L, dto));
    }

    @Test
    void deleteCustomer_success() {
        Customer customer = Customer.builder().id(1L).build();
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        doNothing().when(customerRepository).delete(customer);

        assertDoesNotThrow(() -> customerService.deleteCustomer(1L));
        verify(customerRepository).delete(customer);
    }

    @Test
    void deleteCustomer_notFound() {
        when(customerRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(ResourceNotFoundException.class, () -> customerService.deleteCustomer(1L));
    }

    @Test
    void getCustomersPage_success() {
        Customer customer = Customer.builder().id(1L).username("pageuser").email("p@mail.com").role(Role.CUSTOMER).build();
        Page<Customer> page = new PageImpl<>(List.of(customer));
        when(customerRepository.findAll(any(Pageable.class))).thenReturn(page);

        Page<CustomerResponseDTO> result = customerService.getCustomersPage(PageRequest.of(0, 10));
        assertEquals(1, result.getTotalElements());
        assertEquals("pageuser", result.getContent().get(0).getUsername());
    }
}