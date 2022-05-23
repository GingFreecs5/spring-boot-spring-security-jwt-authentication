package com.eaisign.payload.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data @NoArgsConstructor @AllArgsConstructor
public class File64Response {
	private String name;
	private String pdfB64;
}
