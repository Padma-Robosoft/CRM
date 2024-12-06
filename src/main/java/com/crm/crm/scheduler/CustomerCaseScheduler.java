package com.crm.crm.scheduler;

import com.crm.crm.service.CustomerService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CustomerCaseScheduler {

    private static final Logger logger = LogManager.getLogger(CustomerCaseScheduler.class);

    @Autowired
    private CustomerService customerService;

    @Value("${scheduler.message.fetchCustomerCases}")
    private String fetchCustomerCasesMessage;

    @Value("${scheduler.message.taskCompleted}")
    private String taskCompletedMessage;

    @Value("${scheduler.message.error}")
    private String errorMessage;



    @Scheduled(cron = "0 0 */5 * * *")
    public void fetchCustomerCases() {
        logger.info(fetchCustomerCasesMessage);

        try {
            customerService.fetchCustomerCasesAndLog();
            logger.info(taskCompletedMessage);
        } catch (Exception e) {
            logger.error(errorMessage, e.getMessage());
        }
    }
}