package com.sideforge.controller;

import com.sideforge.dto.admin.*;
import com.sideforge.service.interfaces.AdminService;
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
@RequestMapping("/api/admins")
@Validated
public class AdminController {

    @Autowired
    private AdminService adminService;

    @GetMapping
    @Operation(summary = "Get all administrators", description = "Returns a list of all administrators.")
    public ResponseEntity<List<AdminResponseDTO>> getAllAdmins() {
        return ResponseEntity.ok(adminService.getAllAdmins());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get administrator by ID", description = "Returns the details of an administrator by their ID.")
    public ResponseEntity<AdminResponseDTO> getAdminById(@PathVariable @Positive Long id) {
        return ResponseEntity.ok(adminService.getAdminById(id));
    }

    @PostMapping
    @Operation(summary = "Create administrator", description = "Creates a new administrator and returns it.")
    public ResponseEntity<AdminResponseDTO> createAdmin(@Valid @RequestBody AdminRequestDTO body) {
        AdminResponseDTO created = adminService.createAdmin(body);
        URI location = URI.create("/api/admins/" + created.getId());
        return ResponseEntity.created(location).body(created);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update administrator", description = "Updates the data of an existing administrator.")
    public ResponseEntity<AdminResponseDTO> updateAdmin(@PathVariable @Positive Long id, @Valid @RequestBody AdminUpdateDTO body) {
        return ResponseEntity.ok(adminService.updateAdmin(id, body));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete administrator", description = "Deletes an administrator by their ID.")
    public ResponseEntity<Void> deleteAdmin(@PathVariable @Positive Long id) {
        adminService.deleteAdmin(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/page")
    @Operation(summary = "Get paginated administrators", description = "Returns a paginated list of administrators.")
    public ResponseEntity<Page<AdminResponseDTO>> getAdminsPage(
            @RequestParam(defaultValue = "0") @PositiveOrZero int page,
            @RequestParam(defaultValue = "10") @PositiveOrZero int size,
            @RequestParam(defaultValue = "id,asc") String[] sort
    ) {
        Sort sortObj = Sort.by(Sort.Direction.fromString(sort[1]), sort[0]);
        Pageable pageable = PageRequest.of(page, size, sortObj);
        return ResponseEntity.ok(adminService.getAdminsPage(pageable));
    }
}