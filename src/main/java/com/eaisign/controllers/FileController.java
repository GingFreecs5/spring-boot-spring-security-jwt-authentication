package com.eaisign.controllers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import com.eaisign.exceptions.UserNotFoundException;
import com.eaisign.models.Document;
import com.eaisign.models.Enveloppe;
import com.eaisign.models.User;
import com.eaisign.payload.message.ResponseFile;
import com.eaisign.payload.message.ResponseMessage;
import com.eaisign.payload.request.CreateFolderRequest;
import com.eaisign.payload.request.NewEnvRequest;
import com.eaisign.payload.response.File64Response;
import com.eaisign.repository.UserRepository;
import com.eaisign.security.services.UserDetailsImpl;
import com.eaisign.services.FileStorageService;
import com.eaisign.services.implementations.UserServiceImp;

@Controller

@RequestMapping("/api/files")
@CrossOrigin(origins = "http://localhost:3000",maxAge = 3600)
public class FileController {

	@Autowired
	private FileStorageService fileStorageService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserServiceImp userServiceImp;
	
	

	@PostMapping("/uploadfile")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<File64Response> uploadFile(@RequestParam("file") MultipartFile file) {
		String message = "";
		UserDetailsImpl user =(UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		
		File64Response file64Response=new File64Response("","");
		try {
		
			String file64= fileStorageService.uploadFile(file, user.getId());
			file64Response.setName(file.getOriginalFilename());
			file64Response.setPdfB64(file64);
			message = "Uploaded the file successfully: " + file.getOriginalFilename();
			
			return ResponseEntity.status(HttpStatus.OK).body(file64Response);
		} catch (Exception e) {
			message = "Could not upload the file: " + file.getOriginalFilename() + "!";
			file64Response.setName(message);
			file64Response.setPdfB64("undefined");
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(file64Response);
		}

	}

//	@PostMapping("/upload/{id}/{name}/{status}")
//	public ResponseEntity<?> uploadFiles(@RequestParam("files") MultipartFile[] files,
//			@PathVariable("id") Long id, @PathVariable("name") String name, @PathVariable("status") String status) {
//		File64Response file64Response=new File64Response("","");
//		User user;
//		try {
//			user = userServiceImp.findUser(id);
//			try {
//				List<String> fileNames = new ArrayList<>();
//				Enveloppe envoloppe = fileStorageService.saveEnveloppe(name, status, user);
//				Arrays.asList(files).stream().forEach(file -> {
//					String file64= fileStorageService.uploadFile(file, id);
//					file64Response.setName(file.getOriginalFilename());
//					file64Response.setBase64(file64);
//					 fileStorageService.saveDocument(file.getOriginalFilename(), envoloppe);
//					fileNames.add(file.getOriginalFilename());
//				});
//
//				
//				return ResponseEntity.status(HttpStatus.OK).body(file64Response);
//			} catch (Exception e) {
//				file64Response.setName("undefined");
//				file64Response.setBase64("undefined");
//				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(file64Response);
//			}
//		} catch (UserNotFoundException e1) {
//			message = "User Not Found";
//			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
//
//		}
//	
//	}
	@PostMapping("/saveEnveloppe")                        
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<?> SaveEnveloppe(@RequestBody NewEnvRequest request){
		String message="";
		UserDetailsImpl user =(UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		try {
		User user_=userServiceImp.findUser(user.getId());
		
		try {
			Enveloppe enveloppe = fileStorageService.saveEnveloppe(request.getNom(), request.getStatus(), user_);
			Enveloppe env=fileStorageService.getEnveloppe(4952L);
		for (String file : request.getFiles()) {
			fileStorageService.saveDocument(file, env);			
		}
			
			message="Envelope sauvegardé";
			
			return ResponseEntity.status(HttpStatus.OK).body(enveloppe);
		} catch (Exception e) {
			message="Error";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
		}	
		} catch (UserNotFoundException e1) {
			message = "User Not Found";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));

		}
	}
	
	@PostMapping("/delete/{name}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<ResponseMessage> deleteFile(@PathVariable("name")String name){
		String msg;
		UserDetailsImpl user =(UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		try {
			msg = fileStorageService.deleteFile(name, user.getId());
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(msg));
		} catch (IOException e) {
		
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(e.getMessage()));
		}
	
	}
	
	@PostMapping("/deletefiles")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<ResponseMessage> deleteFiles(@RequestBody String[] files){
		String msg;
		UserDetailsImpl user =(UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		try {
			for(String filename:files) {
				fileStorageService.deleteFile(filename, user.getId());
			}
			msg="files Deleted";
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(msg));
		} catch (IOException e) {
		
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(e.getMessage()));
		}
	}
	
}
