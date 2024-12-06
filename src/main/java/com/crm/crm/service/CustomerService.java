package com.crm.crm.service;

import com.crm.crm.dto.request.CaseRequestDTO;
import com.crm.crm.dto.request.CustomerRequestDTO;
import com.crm.crm.dto.response.APIResponseDTO;
import com.crm.crm.dto.response.InternalAPIResponse;
import com.crm.crm.dto.response.CustomerResponseDTO;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public interface CustomerService {
    ResponseEntity<APIResponseDTO<Map<String, List<CustomerResponseDTO>>>> getAllCustomers();
    ResponseEntity<APIResponseDTO<Map<String, CustomerResponseDTO>>> getCustomerById(Long customerId);
    ResponseEntity<APIResponseDTO<Map<String, String>>> addCustomer(CustomerRequestDTO customerRequest);
    ResponseEntity<APIResponseDTO<Map<String, String>>> updateCustomer(Long customerId, CustomerRequestDTO customerRequest) ;
    ResponseEntity<APIResponseDTO<Map<String, String>>> deleteCustomer(Long customerId);
    ResponseEntity<APIResponseDTO<Map<String, List<CustomerResponseDTO>>>> searchCustomers(String keyword);
   ResponseEntity<InternalAPIResponse> getCasesByCustomerId(Long customerId);
    ResponseEntity<InternalAPIResponse> addCaseForCustomer(Long customerId, CaseRequestDTO caseRequestDTO);
    ResponseEntity<InternalAPIResponse> updateCaseForCustomer(Long caseId, CaseRequestDTO caseRequestDTO);
    public ResponseEntity<InternalAPIResponse> deleteCaseForCustomer(Long caseId);

    void fetchCustomerCasesAndLog();


}
