package com.crm.crm.util;

import com.crm.crm.dto.request.CaseRequestDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@Component
public class WebClientUtil {

    @Value("${case.management.base-url}")
    private String caseManagementBaseUrl;

    @Autowired
    private final WebClient.Builder webClientBuilder;
    private String customerId;

    public WebClientUtil(WebClient.Builder webClientBuilder) {
        this.webClientBuilder = webClientBuilder;
    }


    public String getResponse(String url) {
//                String url = "http://localhost:8081/cases/1";
//                String url = caseManagementBaseUrl+uri;
        return webClientBuilder.build()
                .get()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }


    public String postResponse(String url, CaseRequestDTO payload) {
     //   String url = caseManagementBaseUrl + uri;
        return webClientBuilder.build()
                .post()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(payload)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public String putResponse(String url, CaseRequestDTO caseRequestDTO) {
       // String url = caseManagementBaseUrl + uri;
        return webClientBuilder.build()
                .put()
                .uri(url)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(caseRequestDTO)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }

    public String deleteResponse(String url) {

        return webClientBuilder.build()
                .delete()
                .uri(url)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }


}

