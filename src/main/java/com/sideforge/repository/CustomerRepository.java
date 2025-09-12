package com.sideforge.repository;

import com.sideforge.model.Customer;
import com.sideforge.enums.PreferredLanguage;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CustomerRepository extends JpaRepository<Customer, Long> {
    // Paginated: Find all customers by verified status with pagination and sorting
    // Large customer lists in admin panels, dashboards, or search results
    Page<Customer> findByIsVerified(boolean isVerified, Pageable pageable);

    // Paginated: Find customers by preferred language with pagination and sorting
    Page<Customer> findByPreferredLanguage(PreferredLanguage lang, Pageable pageable);
}