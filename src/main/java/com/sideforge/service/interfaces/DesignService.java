package com.sideforge.service.interfaces;

import com.sideforge.dto.design.*;
import org.springframework.data.domain.*;
import java.util.List;

public interface DesignService {
    // Create a new design from DesignRequestDTO
    DesignResponseDTO createDesign(DesignRequestDTO designRequestDTO);

    // Get a design by its ID
    DesignResponseDTO getDesignById(Long id);

    // Get all designs as a list
    List<DesignResponseDTO> getAllDesigns();

    // Update a design by its ID
    DesignResponseDTO updateDesign(Long id, DesignUpdateDTO designUpdateDTO);

    // Delete a design by its ID
    void deleteDesign(Long id);

    // Get a design by asset ID (1:1 relation)
    DesignResponseDTO getDesignByAssetId(Long assetId);

    // Get paginated designs filtered by asset IDs
    Page<DesignResponseDTO> getDesignsByAssetIds(List<Long> assetIds, Pageable pageable);

    // Get paginated list of all designs
    Page<DesignResponseDTO> getDesignsPage(Pageable pageable);
}