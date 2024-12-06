package com.crm.crm.controller;


import com.crm.crm.dto.request.CaseRequestDTO;
import com.crm.crm.dto.request.CustomerRequestDTO;
import com.crm.crm.dto.response.APIResponseDTO;
import com.crm.crm.dto.response.InternalAPIResponse;
import com.crm.crm.dto.response.CustomerResponseDTO;
import com.crm.crm.service.CustomerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/customers")
public class CustomerController {

    @Autowired
    private CustomerService customerService;

    @PostMapping
    public ResponseEntity<APIResponseDTO<Map<String, String>>> addCustomer(@RequestBody CustomerRequestDTO requestDTO) {
        return customerService.addCustomer(requestDTO);
    }

    @PutMapping("/{customerId}")
    public ResponseEntity<APIResponseDTO<Map<String, String>>> updateCustomer(@PathVariable Long customerId, @RequestBody CustomerRequestDTO requestDTO) {
        return customerService.updateCustomer(customerId, requestDTO);
    }

    @GetMapping
    public ResponseEntity<APIResponseDTO<Map<String, List<CustomerResponseDTO>>>> searchCustomers(
            @RequestParam(required = false) String keyword) {
        return handleSearchCustomers(keyword);
    }

    private ResponseEntity<APIResponseDTO<Map<String, List<CustomerResponseDTO>>>> handleSearchCustomers(String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return customerService.getAllCustomers();
        } else {
            return customerService.searchCustomers(keyword);
        }
    }

    @DeleteMapping("/{customerId}")
    public ResponseEntity<APIResponseDTO<Map<String, String>>> deleteCustomer(@PathVariable Long customerId) {
        return customerService.deleteCustomer(customerId);
    }

    @GetMapping("/{customerId}")
    public ResponseEntity<APIResponseDTO<Map<String, CustomerResponseDTO>>> getCustomerById(@PathVariable Long customerId) {
        return customerService.getCustomerById(customerId);
    }

    @GetMapping("/{customerId}/cases")
    public ResponseEntity<InternalAPIResponse> getCasesByCustomerId(@PathVariable Long customerId)  {
        return customerService.getCasesByCustomerId(customerId);
    }

    @PostMapping("/{customerId}/cases")
    public ResponseEntity<InternalAPIResponse> addCaseForCustomer(
            @PathVariable Long customerId,
            @RequestBody CaseRequestDTO caseRequestDTO) {
        return customerService.addCaseForCustomer(customerId, caseRequestDTO);
    }

    @PutMapping("/cases/{caseId}")
    public ResponseEntity<InternalAPIResponse> updateCaseForCustomer(
            @PathVariable Long caseId,
            @RequestBody CaseRequestDTO caseRequestDTO) {
        return customerService.updateCaseForCustomer(caseId, caseRequestDTO);
    }

    @DeleteMapping("/cases/{caseId}")
    public ResponseEntity<InternalAPIResponse> deleteCaseForCustomer(@PathVariable Long caseId) {
        return customerService.deleteCaseForCustomer(caseId);
    }

}
