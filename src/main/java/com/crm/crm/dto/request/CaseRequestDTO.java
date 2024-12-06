package com.crm.crm.dto.request;

import com.crm.crm.model.enums.Priority;
import com.crm.crm.model.enums.Status;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class CaseRequestDTO {
    private String title;
    private String description;
    private Status status;
    private Priority priority;
    private String assignedTo;
    private Long customerId;
}
