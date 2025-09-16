package com.sideforge.repository;

import com.sideforge.model.Design;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface DesignRepository extends JpaRepository<Design, Long> {
    // Find design by asset ID
    Optional<Design> findByAsset_Id(Long assetId);

    // Paginated: Find all designs by asset IDs with pagination and sorting
    Page<Design> findAllByAsset_IdIn(List<Long> assetIds, Pageable pageable);
}
