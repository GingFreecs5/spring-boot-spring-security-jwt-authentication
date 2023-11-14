package com.eaisign.payload.message;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class FilesbyEnveloppeIdResponse {
	
		private String name;
		private boolean isUploading;
		private String b64;
}
