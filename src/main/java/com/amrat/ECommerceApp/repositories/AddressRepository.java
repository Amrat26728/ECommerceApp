package com.amrat.ECommerceApp.repositories;

import com.amrat.ECommerceApp.entities.Address;
import com.amrat.ECommerceApp.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface AddressRepository extends JpaRepository<Address, Long> {
    List<Address> findByUser(User user);

    Address findByIdAndUserId(Long addressId, Long userId);

    @Modifying
    @Query("""
            UPDATE Address a
            SET a.isDefault = false
            WHERE a.user.id = :userId
            AND a.id <> :addressId
            """)
    void unsetAllOtherAddresses(@Param("userId") Long userId, @Param("addressId") Long addressId);
}
