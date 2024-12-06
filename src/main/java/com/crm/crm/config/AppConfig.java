package com.crm.crm.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClient.Builder;

  @Configuration
  public class AppConfig {

    @Bean
    public WebClient.Builder webClientBuilder() {
      return WebClient.builder();
    }
  }
