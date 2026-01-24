package com.amrat.ECommerceApp.dtos.address;

import lombok.*;

@Data
@Getter
@Builder
@AllArgsConstructor
public class AddAddressRequestDto {

    private String streetAddress;
    private String landmark; // optional
    private String city;
    private String state;
    private String postalCode;
    private Boolean isDefault;

}
