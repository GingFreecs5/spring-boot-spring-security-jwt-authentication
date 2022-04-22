package com.eaisign.payload.request;

import lombok.Data;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

@Data
public class CheckEmailRequest {
    @NotBlank
    @Email
    private String email;
}
