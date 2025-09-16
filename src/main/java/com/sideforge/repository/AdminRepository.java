package com.sideforge.repository;

import com.sideforge.model.Admin;
import com.sideforge.enums.Department;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AdminRepository extends JpaRepository<Admin, Long> {
    // Find all admins by department
    List<Admin> findByDepartment(Department department);

    // Find all admins with admin level greater or equal to given value
    List<Admin> findByAdminLevelGreaterThanEqual(Integer adminLevel);
}
