package com.sideforge.util;

import com.sideforge.model.*;
import com.sideforge.enums.*;
import com.sideforge.repository.*;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class DataLoader implements CommandLineRunner {

    @Autowired
    private final UserRepository userRepository;
    @Autowired
    private final AdminRepository adminRepository;
    @Autowired
    private final CustomerRepository customerRepository;
    @Autowired
    private final AssetRepository assetRepository;
    @Autowired
    private final DesignRepository designRepository;
    @Autowired
    private final SceneRepository sceneRepository;

    @Override
    @Transactional
    public void run(String... args) {
        // Declared here (scope) to use them in other data initialization
        Customer customer1 = null;
        Customer customer2 = null;
        Admin admin1;
        Admin admin2;

        // Admins
        if (adminRepository.count() == 0) {
            admin1 = new Admin();
            admin1.setUsername("admin1");
            admin1.setPasswordHash("adminpass1");
            admin1.setEmail("admin1@sideforge.com");
            admin1.setRole(Role.ADMIN);

            admin2 = new Admin();
            admin2.setUsername("admin2");
            admin2.setPasswordHash("adminpass2");
            admin2.setEmail("admin2@sideforge.com");
            admin2.setRole(Role.ADMIN);

            adminRepository.save(admin1);
            adminRepository.save(admin2);
        }

        // Customers
        if (customerRepository.count() == 0) {
            customer1 = new Customer();
            customer1.setUsername("customer1");
            customer1.setPasswordHash("custpass1");
            customer1.setEmail("customer1@sideforge.com");
            customer1.setRole(Role.CUSTOMER);

            customer2 = new Customer();
            customer2.setUsername("customer2");
            customer2.setPasswordHash("custpass2");
            customer2.setEmail("customer2@sideforge.com");
            customer2.setRole(Role.CUSTOMER);

            customerRepository.save(customer1);
            customerRepository.save(customer2);
        } else {
            customer1 = customerRepository.findByUsername("customer1").orElse(null);
            customer2 = customerRepository.findByUsername("customer2").orElse(null);
        }

        // Assets, Designs and Scenes
        if (assetRepository.count() == 0
                && designRepository.count() == 0
                && sceneRepository.count() == 0) {
            // Asset 1
            Asset asset1 = new Asset();
            asset1.setName("3D Mug");
            asset1.setDescription("Customizable 3D mug");
            asset1.setGlbPath("/assets/3d/mug.glb");
            asset1.setThumbnailDefault("/assets/img/mug-thumb.png");
            asset1.setPartsConfigJson("{\"handle\":true,\"color\":\"white\"}");
            assetRepository.save(asset1);

            // Asset 2
            Asset asset2 = new Asset();
            asset2.setName("3D T-Shirt");
            asset2.setDescription("Basic customizable t-shirt");
            asset2.setGlbPath("/assets/3d/tshirt.glb");
            asset2.setThumbnailDefault("/assets/img/tshirt-thumb.png");
            asset2.setPartsConfigJson("{\"size\":\"M\",\"color\":\"black\"}");
            assetRepository.save(asset2);

            // Design 1 linked to asset1
            Design design1 = new Design();
            design1.setName("Modernist Mug");
            design1.setTextureMapUrl("/textures/mug-modernist.png");
            design1.setMaterialsJson("{\"material\":\"ceramic\"}");
            design1.setPartsColorsJson("{\"handle\":\"blue\"}");
            design1.setLogoConfigJson("{\"logo\":\"/logos/modern.png\"}");
            design1.setTextConfigJson("{\"text\":\"Hello Mug\"}");
            design1.setAsset(asset1);
            designRepository.save(design1);

            // Design 2 linked to asset2
            Design design2 = new Design();
            design2.setName("Urban T-Shirt");
            design2.setTextureMapUrl("/textures/tshirt-urban.png");
            design2.setMaterialsJson("{\"material\":\"cotton\"}");
            design2.setPartsColorsJson("{\"sleeve\":\"black\"}");
            design2.setLogoConfigJson("{\"logo\":\"/logos/urban.png\"}");
            design2.setTextConfigJson("{\"text\":\"Urban Style\"}");
            design2.setAsset(asset2);
            designRepository.save(design2);

            // Retrieve designs managed by JPA
            Design managedDesign1 = designRepository.findById(design1.getId()).orElseThrow();
            Design managedDesign2 = designRepository.findById(design2.getId()).orElseThrow();

            // Retrieve customers managed by JPA
            Customer managedCustomer1 = customerRepository.findByUsername("customer1").orElse(null);
            Customer managedCustomer2 = customerRepository.findByUsername("customer2").orElse(null);

            // Scene 1 linked to design1 (user customer1)
            Scene scene1 = new Scene();
            scene1.setName("Mug Scene");
            scene1.setLightingConfigJson("{\"type\":\"modern\"}");
            scene1.setCameraConfigJson("{\"angle\":45}");
            scene1.setThumbnail("/scenes/mug-scene-thumb.png");
            scene1.setOwner(managedCustomer1);
            scene1.setDesign(managedDesign1);
            scene1.setCreatedAt(LocalDateTime.now());
            scene1.setUpdatedAt(LocalDateTime.now());
            sceneRepository.save(scene1);

            // Scene 2 linked to design2 (user customer2)
            Scene scene2 = new Scene();
            scene2.setName("T-Shirt Scene");
            scene2.setLightingConfigJson("{\"type\":\"shop\"}");
            scene2.setCameraConfigJson("{\"angle\":30}");
            scene2.setThumbnail("/scenes/tshirt-scene-thumb.png");
            scene2.setOwner(managedCustomer2);
            scene2.setDesign(managedDesign2);
            scene2.setCreatedAt(LocalDateTime.now());
            scene2.setUpdatedAt(LocalDateTime.now());
            sceneRepository.save(scene2);
        }
    }
}
