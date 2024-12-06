package com.crm.crm.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class CustomerRequestDTO {

    private String firstName;
    private String lastName;
    private String phone;
    private String email;
    private String address;
}
