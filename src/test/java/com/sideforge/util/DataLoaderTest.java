package com.sideforge.util;

import com.sideforge.model.Admin;
import com.sideforge.repository.*;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class DataLoaderTest {

    @Autowired
    private DataLoader dataLoader;
    @Autowired
    private AdminRepository adminRepository;
    @Autowired
    private CustomerRepository customerRepository;
    @Autowired
    private AssetRepository assetRepository;
    @Autowired
    private DesignRepository designRepository;
    @Autowired
    private SceneRepository sceneRepository;

    @BeforeEach
    void setUp() {
        // Delete in strict dependency order
        sceneRepository.deleteAll();
        designRepository.deleteAll();
        assetRepository.deleteAll();
        adminRepository.deleteAll();
        customerRepository.deleteAll();
    }

    @Test
    void run_shouldLoadInitialData() {
        // Execute DataLoader to insert initial data
        dataLoader.run();

        // Check that two admins are loaded and their usernames
        List<Admin> admins = adminRepository.findAll();
        assertEquals(2, admins.size());
        List<String> usernames = admins.stream().map(Admin::getUsername).collect(Collectors.toList());
        assertTrue(usernames.contains("admin1"));
        assertTrue(usernames.contains("admin2"));

        // Check that two customers are loaded and their usernames
        assertEquals(2, customerRepository.count());
        assertTrue(customerRepository.findByUsername("customer1").isPresent());
        assertTrue(customerRepository.findByUsername("customer2").isPresent());

        // Check that two assets, designs, and scenes are loaded
        assertEquals(2, assetRepository.count());
        assertEquals(2, designRepository.count());
        assertEquals(2, sceneRepository.count());
    }
}