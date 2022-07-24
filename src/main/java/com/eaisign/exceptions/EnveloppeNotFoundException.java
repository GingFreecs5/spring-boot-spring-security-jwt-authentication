package com.eaisign.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND, reason = "Enveloppe Not Found")
public class EnveloppeNotFoundException extends RuntimeException {
		public EnveloppeNotFoundException() {
			super("Enveloppe Not Found");
		}
}
