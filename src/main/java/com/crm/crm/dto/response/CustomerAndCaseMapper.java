package com.crm.crm.dto.response;

import com.crm.crm.model.Customer;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;

import java.security.PrivateKey;
import java.util.ArrayList;

@Getter
@Setter
@AllArgsConstructor
public class CustomerAndCaseMapper {
    private Customer customer;
    private ArrayList<CaseResponseDTO> cases;
}
