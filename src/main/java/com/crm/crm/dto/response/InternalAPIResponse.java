package com.crm.crm.dto.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
//@AllArgsConstructor
//@NoArgsConstructor
public class InternalAPIResponse {
    @JsonProperty("status")
    private int status;
    @JsonProperty("code")
    private int code;
    @JsonProperty("message")
    private String message;
    @JsonProperty("data")
    private Object data;

    public InternalAPIResponse(int status, int code, String message, Object data) {
        this.status = status;
        this.code = code;
        this.message = message;
        this.data = data;
    }
    public InternalAPIResponse(){
    }
}

