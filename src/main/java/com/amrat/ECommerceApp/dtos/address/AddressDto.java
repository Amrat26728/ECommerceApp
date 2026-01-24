package com.amrat.ECommerceApp.dtos.address;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AddressDto {
    private Long id;
    private String streetAddress;
    private String landmark;
    private String city;
    private String state;
    private String postalCode;
    private boolean isDefault;
}
