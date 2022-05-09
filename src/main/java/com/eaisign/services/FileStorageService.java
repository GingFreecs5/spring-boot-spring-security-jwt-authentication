package com.eaisign.services;

import java.io.IOException;
import java.util.Base64;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.eaisign.models.FileDB;
import com.eaisign.repository.FileDBRepository;

@Service
public class FileStorageService {
	 @Autowired
	 private FileDBRepository fileDBRepository;
	 public FileDB store(MultipartFile file) throws IOException {
		 String fileName=StringUtils.cleanPath(file.getOriginalFilename());
		 String b64=Base64.getEncoder().encodeToString(file.getBytes());
		 FileDB FileDB=new FileDB(fileName,file.getContentType(),file.getBytes(),b64);
		 System.out.println(b64);
		 return fileDBRepository.save(FileDB);
	 }
	 public FileDB getFile(String id) {
		 return fileDBRepository.findById(id).get();
	 }
	 public Stream<FileDB> getAllFiles(){
		 return fileDBRepository.findAll().stream();
	 }
	 
}
