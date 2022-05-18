package com.eaisign.controllers;

import java.util.List;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.eaisign.payload.message.ResponseFile;
import com.eaisign.payload.message.ResponseMessage;
import com.eaisign.payload.request.CreateFolderRequest;
import com.eaisign.services.FileStorageService;

@Controller

@RequestMapping("/api/auth")

public class FileController {

		@Autowired 
		private FileStorageService fileStorageService;
		@PostMapping("/createfolder")
		public ResponseEntity<ResponseMessage> createFolder(@RequestBody CreateFolderRequest request){
			String msg=fileStorageService.CreateDirectory(request.getName(), request.getId());
		    return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(msg));
			}
		
		@PostMapping("/savefile/{path}")
		public ResponseEntity<ResponseMessage> saveFile(@RequestParam("file") MultipartFile file,@PathVariable("path") String path){
			/*String message="";
			try {
			      fileStorageService.save(file,path);
			      message = "Uploaded the file successfully: " + file.getOriginalFilename();
			      return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
			    } catch (Exception e) {
			      message = "Could not upload the file: " + file.getOriginalFilename() + "!";
			      return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
			    }*/
			return null;
			
		}
}
