package com.eaisign.exceptions;


import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Signataire Not Found")
public class SignataireNotFound extends RuntimeException{
	public SignataireNotFound() {
		super("Signataire Not Found");
	}
}
