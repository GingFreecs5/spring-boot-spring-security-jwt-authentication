package com.eaisign.payload.response;


import lombok.Data;

@Data
public class ServiceResponse {

    private String status;
    private String description;
    private Object data;
}
