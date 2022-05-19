package com.eaisign.payload.request;

import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import lombok.Data;

@Data
public class NewEnvRequest {
	private String nom;
	private String status;
	private Long id;
	private MultipartFile[] files;
}
