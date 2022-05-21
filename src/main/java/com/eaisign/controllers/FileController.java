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
import com.eaisign.models.Envoloppe;
import com.eaisign.models.User;
import com.eaisign.payload.message.ResponseFile;
import com.eaisign.payload.message.ResponseMessage;
import com.eaisign.payload.request.CreateFolderRequest;
import com.eaisign.payload.request.NewEnvRequest;
import com.eaisign.repository.UserRepository;
import com.eaisign.services.FileStorageService;
import com.eaisign.services.implementations.UserServiceImp;

@Controller

@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:3000",maxAge = 3600)
public class FileController {

	@Autowired
	private FileStorageService fileStorageService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private UserServiceImp userServiceImp;
	
	@PostMapping("/createfolder")
	public ResponseEntity<ResponseMessage> createFolder(@RequestBody CreateFolderRequest request) {
		String msg = fileStorageService.CreateDirectory(request.getId());
		return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(msg));
	}

	@PostMapping("/savefile/{id}")
	public ResponseEntity<ResponseMessage> saveFile(@RequestParam("file") MultipartFile file,
			@PathVariable("id") Long id) {
		String message = "";
		try {
			fileStorageService.saveDocument(file, id);
			message = "Uploaded the file successfully: " + file.getOriginalFilename();
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
		} catch (Exception e) {
			message = "Could not upload the file: " + file.getOriginalFilename() + "!";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
		}

	}

	@PostMapping("/upload/{id}/{name}/{status}")
	public ResponseEntity<ResponseMessage> uploadFiles(@RequestParam("files") MultipartFile[] files,
			@PathVariable("id") Long id, @PathVariable("name") String name, @PathVariable("status") String status) {
		String message = "";
		User user;
		try {
			user = userServiceImp.findUser(id);
			try {
				List<String> fileNames = new ArrayList<>();
				Envoloppe envoloppe = fileStorageService.saveEnvoloppe(name, status, user);
				Arrays.asList(files).stream().forEach(file -> {
					fileStorageService.saveDocument(file, id);
					fileStorageService.saveDocument(file.getOriginalFilename(), envoloppe);
					fileNames.add(file.getOriginalFilename());
				});

				message = "Uploaded the files successfully: " + fileNames;
				return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(message));
			} catch (Exception e) {
				message = "Fail to upload files!";
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
			}
		} catch (UserNotFoundException e1) {
			message = "User Not Found";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));

		}
	
	}
	
	@PostMapping("/delete/{id}/{name}")
	public ResponseEntity<ResponseMessage> deleteFile(@PathVariable("id")Long id,@PathVariable("name")String name){
		String msg;
		try {
			msg = fileStorageService.deleteFile(name, id);
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(msg));
		} catch (IOException e) {
		
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(e.getMessage()));
		}
	
	}
}
