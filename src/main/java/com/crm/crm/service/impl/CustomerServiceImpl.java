package com.crm.crm.service.impl;

import com.crm.crm.dto.request.CaseRequestDTO;
import com.crm.crm.dto.request.CustomerRequestDTO;
import com.crm.crm.dto.response.*;
import com.crm.crm.exception.ResourceNotFoundException;
import com.crm.crm.model.Customer;
import com.crm.crm.repository.CustomerRepository;
import com.crm.crm.service.CustomerService;
import com.crm.crm.util.APIResponseUtil;
import com.crm.crm.util.WebClientUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClientResponseException;

import java.util.*;
import java.util.stream.Collectors;

@Service
public class CustomerServiceImpl implements CustomerService {

    private final CustomerRepository customerRepository;
    private static final Logger logger = LogManager.getLogger(CustomerService.class);
    private WebClientUtil webClientUtil;



    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, WebClientUtil webClientUtil) {
        this.customerRepository = customerRepository;
        this.webClientUtil = webClientUtil;
    }

      private String uri;

    @Value("${scheduler.message.fetchCustomerCases}")
    private String fetchCustomerCasesMessage;

    @Value("${scheduler.message.taskCompleted}")
    private String taskCompletedMessage;

    @Value("${scheduler.message.error}")
    private String errorMessage;

    @Value("${error.customer.not-found}")
    private String customerNotFoundMessage;

    @Value("${response.customerAdded}")
    private String customerAddedMessage;

    @Value("${response.CustomerById}")
    private String customerByIdMessage;

    @Value("${response.customerUpdated}")
    private String customerUpdatedMessage;

    @Value("${response.customerDeleted}")
    private String customerDeletedMessage;

    @Value("${log.customer.add}")
    private String logAddCustomer;

    @Value("${log.customer.update}")
    private String logUpdateCustomer;

    @Value("${log.customer.delete}")
    private String logDeleteCustomer;

    @Value("${log.customer.getAll}")
    private String logGetAllCustomers;

    @Value("${log.customer.getById}")
    private String logGetCustomerById;

    @Value("${log.customer.search}")
    private String logSearchCustomers;

    @Value("${response.customerWithCases}")
    private String customerWithCasesMessage;

    @Value("${log.customer.fetch}")
    private String logCustomerFetch;

    @Value("${log.customer.noCasesFound}")
    private String logCustomerNoCasesFound;

    @Value("${cases.getCustomerUri}")
    private String getCustomerUri;

    @Value("${cases.createUri}")
    private String createCaseUri;

    @Value("${cases.updateUri}")
    private String updateCaseUri;

    @Value("${cases.deleteUri}")
    private String deleteCaseUri;

    @Value("${caseCreationInitiated}")
    private String caseCreationInitiatedMessage;

    @Value("${caseCreatedSuccess}")
    private String caseCreatedSuccessMessage;

    @Value("${caseUpdatedSuccess}")
    private String caseUpdatedSuccessMessage;

    @Value("${caseDeletedSuccess}")
    private String caseDeletedSuccessMessage;

    @Value("${errorCreatingCase}")
    private String errorCreatingCaseMessage;

    @Value("${errorUpdatingCase}")
    private String errorUpdatingCaseMessage;

    @Value("${errorDeletingCase}")
    private String errorDeletingCaseMessage;

    @Value("${unexpectedError}")
    private String unexpectedErrorMessage;

    @Value("${errorCreatingCaseWithDetails}")
    private String errorCreatingCaseWithDetails;

    @Value("${unexpectedErrorWhileCreatingCase}")
    private String unexpectedErrorWhileCreatingCase;

    @Value("${internalServerError}")
    private String internalServerError;

    @Value("${case.management.url}")
    private String baseUrl;


    private CustomerResponseDTO mapToCustomerResponseDTO(Customer customer) {
        return new CustomerResponseDTO(
                customer.getId(),
                customer.getFirstName(),
                customer.getLastName(),
                customer.getPhone(),
                customer.getEmail(),
                customer.getAddress());
    }

    @Override
    public ResponseEntity<APIResponseDTO<Map<String, List<CustomerResponseDTO>>>> getAllCustomers() {
        logger.info(logGetAllCustomers);
        List<Customer> customers = customerRepository.findAll();
        List<CustomerResponseDTO> responseDTOs = customers.stream()
                .map(this::mapToCustomerResponseDTO)
                .collect(Collectors.toList());
        Map<String, List<CustomerResponseDTO>> responseData = new HashMap<>();
        responseData.put("customers", responseDTOs);
        return ResponseEntity.ok(APIResponseUtil.successResponse(responseData));
    }

    @Override
    public ResponseEntity<APIResponseDTO<Map<String, CustomerResponseDTO>>> getCustomerById(Long customerId) {
        logger.info(String.format(logGetCustomerById,customerId));
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> {
                    System.out.println("ERROR");
                    logger.error(String.format(customerNotFoundMessage, customerId));
                    return new ResourceNotFoundException(String.format(customerNotFoundMessage, customerId));
                });
        logger.debug("Its customer "+customer);
        CustomerResponseDTO responseDTO = mapToCustomerResponseDTO(customer);
        logger.debug("its DTO "+responseDTO);
        Map<String, CustomerResponseDTO> responseData = new HashMap<>();
        responseData.put("customer", responseDTO);
        return ResponseEntity.ok(APIResponseUtil.successResponse(responseData));
    }

    @Override
    public ResponseEntity<APIResponseDTO<Map<String, String>>> addCustomer(CustomerRequestDTO customerRequest) {
        logger.info(logAddCustomer, customerRequest);
        Customer customer = new Customer();
        customer.setFirstName(customerRequest.getFirstName());
        customer.setLastName(customerRequest.getLastName());
        customer.setPhone(customerRequest.getPhone());
        customer.setEmail(customerRequest.getEmail());
        customer.setAddress(customerRequest.getAddress());
        customerRepository.save(customer);
        return ResponseEntity.status(201).body(APIResponseUtil.successResponse(customerAddedMessage));
    }

    @Override
    public ResponseEntity<APIResponseDTO<Map<String, String>>> updateCustomer(Long customerId, CustomerRequestDTO customerRequest) {
        logger.info(logUpdateCustomer, customerId);
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> {
                    logger.error(customerNotFoundMessage, customerId);
                    return new ResourceNotFoundException(customerNotFoundMessage);
                });
        updateCustomerDetails(customer, customerRequest);
        customerRepository.save(customer);
        return ResponseEntity.ok(APIResponseUtil.successResponse(customerUpdatedMessage));
    }

    private void updateCustomerDetails(Customer customer, CustomerRequestDTO customerRequest) {
        customer.setFirstName(customerRequest.getFirstName());
        customer.setLastName(customerRequest.getLastName());
        customer.setPhone(customerRequest.getPhone());
        customer.setEmail(customerRequest.getEmail());
        customer.setAddress(customerRequest.getAddress());
    }

    @Override
    public ResponseEntity<APIResponseDTO<Map<String, List<CustomerResponseDTO>>>> searchCustomers(String keyword) {
        logger.info(logSearchCustomers, keyword);
        List<Customer> customers = customerRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(keyword, keyword);
        List<CustomerResponseDTO> responseDTOs = customers.stream()
                .map(this::mapToCustomerResponseDTO)
                .collect(Collectors.toList());
        Map<String, List<CustomerResponseDTO>> responseData = new HashMap<>();
        responseData.put("customers", responseDTOs);
        return ResponseEntity.ok(APIResponseUtil.successResponse(responseData));
    }

    @Override
    public ResponseEntity<APIResponseDTO<Map<String, String>>> deleteCustomer(Long customerId) {
        logger.info(logDeleteCustomer, customerId);
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> {
                    logger.error(customerNotFoundMessage, customerId);
                    return new ResourceNotFoundException(customerNotFoundMessage);
                });
        customerRepository.delete(customer);
        return ResponseEntity.ok(APIResponseUtil.successResponse(customerDeletedMessage));
    }

    public ResponseEntity<InternalAPIResponse> getCasesByCustomerId(Long customerId) {
//        String uri = getCustomerUri+ customerId;
        String url = baseUrl+getCustomerUri+customerId;
        String responseJsonInString = webClientUtil.getResponse(url);
        ObjectMapper objectMapper = new ObjectMapper();
        try {
            InternalAPIResponse caseResponse = objectMapper.readValue(responseJsonInString, InternalAPIResponse.class);
            ArrayList<CaseResponseDTO> caseResponseDTO = (ArrayList<CaseResponseDTO>) caseResponse.getData();
            logger.info(caseResponse);
            Optional<Customer> customer = customerRepository.findById(customerId);
            CustomerAndCaseMapper customerAndCaseMapper = new CustomerAndCaseMapper(customer.get(),caseResponseDTO);
            InternalAPIResponse internalApiResponse = new InternalAPIResponse(0,200,"Success",customerAndCaseMapper);
            return new ResponseEntity<>(internalApiResponse, HttpStatus.OK);
        }
        catch (Exception err){
            logger.error(err);
        }
        return null;

    }

    @Override
    public ResponseEntity<InternalAPIResponse> addCaseForCustomer(Long customerId, CaseRequestDTO caseRequestDTO) {
        String uri = baseUrl+createCaseUri+customerId;
        ObjectMapper objectMapper = new ObjectMapper();
        try {
//            logger.info(String.format(caseCreationInitiatedMessage,customerId), customerId);
//            Customer customer = customerRepository.findById(customerId)
//                    .orElseThrow(() -> {
//                        logger.error(customerNotFoundMessage, customerId);
//                        return new ResourceNotFoundException(customerNotFoundMessage);
//                    });
//            caseRequestDTO.setCustomerId(customerId);
//            String requestPayload = objectMapper.writeValueAsString(caseRequestDTO);
            String responseJsonInString = webClientUtil.postResponse(uri, caseRequestDTO);
            InternalAPIResponse caseResponse = objectMapper.readValue(responseJsonInString, InternalAPIResponse.class);
            logger.info(customerAddedMessage, caseResponse);
            InternalAPIResponse internalApiResponse = new InternalAPIResponse(0, 201, caseCreatedSuccessMessage, caseResponse.getData());
            return new ResponseEntity<>(internalApiResponse, HttpStatus.CREATED);
        } catch (ResourceNotFoundException e) {
            logger.error(customerNotFoundMessage, customerId);
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(new InternalAPIResponse(1, 404, customerNotFoundMessage, null));
        } catch (WebClientResponseException e) {
            logger.error(String.format(errorCreatingCaseWithDetails, e.getStatusCode(), e.getResponseBodyAsString()));
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new InternalAPIResponse(2, 500, errorCreatingCaseMessage+ e.getResponseBodyAsString(), null));
        } catch (Exception err) {
            logger.error(String.format(unexpectedErrorWhileCreatingCase, customerId)+ " "+ err.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new InternalAPIResponse(2, 500, internalServerError+ err.getMessage(), null));
        }
    }

    @Override
    public ResponseEntity<InternalAPIResponse> updateCaseForCustomer(Long caseId, CaseRequestDTO caseRequestDTO) {
        String uri =baseUrl+updateCaseUri+caseId;
        try {

            String existingCaseJson = webClientUtil.putResponse(uri, caseRequestDTO);
            ObjectMapper objectMapper = new ObjectMapper();
            InternalAPIResponse existingCaseResponse = objectMapper.readValue(existingCaseJson, InternalAPIResponse.class);
//            caseRequestDTO.setCustomerId(customerId);
//            String updatedCasePayload = objectMapper.writeValueAsString(caseRequestDTO);
//            String responseJsonInString = webClientUtil.putResponse(uri, updatedCasePayload);
//            InternalAPIResponse caseResponse = objectMapper.readValue(responseJsonInString, InternalAPIResponse.class);
            logger.info((caseUpdatedSuccessMessage), existingCaseResponse);
            return new ResponseEntity<>(new InternalAPIResponse(0, 200, caseUpdatedSuccessMessage, existingCaseResponse.getData()), HttpStatus.OK);

        } catch (Exception e) {
            logger.error(errorUpdatingCaseMessage, e.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new InternalAPIResponse(2, 500, errorUpdatingCaseMessage + e.getMessage(), null));
        }
    }


    @Override
    public ResponseEntity<InternalAPIResponse> deleteCaseForCustomer(Long caseId) {
        String uri = baseUrl+deleteCaseUri + caseId;
        String responseJsonInString = webClientUtil.deleteResponse(uri);
        logger.info("Response :"+responseJsonInString);
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            InternalAPIResponse caseResponse = objectMapper.readValue(responseJsonInString, InternalAPIResponse.class);
            logger.info(caseDeletedSuccessMessage, caseResponse);
            InternalAPIResponse internalApiResponse = new InternalAPIResponse(0, 200, caseDeletedSuccessMessage, caseResponse.getData());
            return new ResponseEntity<>(internalApiResponse, HttpStatus.OK);

        } catch (Exception err) {
            logger.error(errorDeletingCaseMessage, err.getMessage());
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new InternalAPIResponse(2, 500, errorDeletingCaseMessage, null));
        }
    }


    public void fetchCustomerCasesAndLog() {
        logger.info(fetchCustomerCasesMessage);

        try {
            List<Customer> customers = customerRepository.findAll();
            customers.forEach(customer -> {
                logger.info("Customer ID: {}, Name: {} {}", customer.getId(), customer.getFirstName(), customer.getLastName());
            });
            logger.info(taskCompletedMessage);
        } catch (Exception e) {
            logger.error(String.format(errorMessage, e.getMessage()));
        }
    }

}

