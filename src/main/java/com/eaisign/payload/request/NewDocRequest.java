package com.eaisign.payload.request;

import lombok.Data;

@Data
public class NewDocRequest {
	private String[] files;
	private Long idEnveloppe;
	private String canalUtilise;
	private String email;
	private boolean copyFiles;
}
