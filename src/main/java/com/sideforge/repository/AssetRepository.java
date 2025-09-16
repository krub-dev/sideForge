package com.sideforge.repository;

import com.sideforge.model.Asset;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssetRepository extends JpaRepository<Asset, Long> {
    // Paginated: Find assets by partial name match with pagination and sorting
    // Asset catalogs and search with pagination
    Page<Asset> findByNameContainingIgnoreCase(String name, Pageable pageable);

    // Check if an asset exists by GLB path - recommended for asset validation
    boolean existsByGlbPath(String glbPath);
}
