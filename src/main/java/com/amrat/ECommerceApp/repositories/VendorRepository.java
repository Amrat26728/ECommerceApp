package com.amrat.ECommerceApp.repositories;

import com.amrat.ECommerceApp.entities.Vendor;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VendorRepository extends JpaRepository<Vendor, Long> {
}
