package com.eaisign.payload.request;

import lombok.Data;

@Data
public class NewDocRequest {
	private String[] files;
	private Long idEnveloppe;
	private String canalUtilise;
	private String signataireEmail;
	private String signataireNom;
	private String signatairePrenom;
	private boolean copyFiles;
}
