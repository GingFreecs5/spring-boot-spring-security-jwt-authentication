package com.eaisign.payload.request;

import lombok.Data;

@Data
public class NewDocRequest {
	public String[] files;
	public Long idEnveloppe;
	public String canalUtilise;
	public String email;
}
