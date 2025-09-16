package com.sideforge.controller;

import com.sideforge.dto.customer.*;
import com.sideforge.service.interfaces.CustomerService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.security.test.context.support.WithMockUser;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;

@WebMvcTest(CustomerController.class)
class CustomerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerService customerService;

    @WithMockUser
    @Test
    // Tests retrieving all customers returns the correct list.
    void getAllCustomers() throws Exception {
        CustomerResponseDTO customer = new CustomerResponseDTO();
        customer.setId(1L);
        customer.setUsername("testcustomer");
        when(customerService.getAllCustomers()).thenReturn(List.of(customer));

        mockMvc.perform(get("/api/customers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("testcustomer"));
    }

    @WithMockUser
    @Test
    // Tests retrieving a customer by ID returns the correct customer.
    void getCustomerById() throws Exception {
        CustomerResponseDTO customer = new CustomerResponseDTO();
        customer.setId(1L);
        customer.setUsername("testcustomer");
        when(customerService.getCustomerById(1L)).thenReturn(customer);

        mockMvc.perform(get("/api/customers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testcustomer"));
    }

    @WithMockUser
    @Test
    // Tests creating a new customer returns the created customer and correct location header.
    void createCustomer() throws Exception {
        CustomerRequestDTO request = new CustomerRequestDTO();
        // Check fields if necessary

        CustomerResponseDTO created = new CustomerResponseDTO();
        created.setId(1L);
        created.setUsername("newcustomer");

        when(customerService.createCustomer(any(CustomerRequestDTO.class))).thenReturn(created);

        mockMvc.perform(post("/api/customers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"newcustomer\",\"email\":\"new@customer.com\",\"role\":\"CUSTOMER\",\"password\":\"12345678\"}")
                        .with(csrf()))
                .andExpect(status().isCreated())
                .andExpect(header().string("Location", "/api/customers/1"))
                .andExpect(jsonPath("$.username").value("newcustomer"));
    }

    @WithMockUser
    @Test
    // Tests updating a customer returns the updated customer.
    void updateCustomer() throws Exception {
        CustomerResponseDTO updated = new CustomerResponseDTO();
        updated.setId(1L);
        updated.setUsername("updatedcustomer");

        when(customerService.updateCustomer(eq(1L), any(CustomerUpdateDTO.class))).thenReturn(updated);

        mockMvc.perform(put("/api/customers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"username\":\"updatedcustomer\",\"email\":\"updated@customer.com\",\"role\":\"CUSTOMER\"}")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("updatedcustomer"));
    }

    @WithMockUser
    @Test
    // Tests deleting a customer returns no content status.
    void deleteCustomer() throws Exception {
        doNothing().when(customerService).deleteCustomer(1L);

        mockMvc.perform(delete("/api/customers/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());
    }

    @WithMockUser
    @Test
    // Tests retrieving a paginated list of customers returns the correct page.
    void getCustomersPage() throws Exception {
        CustomerResponseDTO customer = new CustomerResponseDTO();
        customer.setId(1L);
        customer.setUsername("testcustomer");
        Page<CustomerResponseDTO> page = new PageImpl<>(List.of(customer));
        when(customerService.getCustomersPage(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/api/customers/page")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "id,asc"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].username").value("testcustomer"));
    }
}