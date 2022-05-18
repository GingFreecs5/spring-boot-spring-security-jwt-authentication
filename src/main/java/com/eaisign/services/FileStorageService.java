package com.eaisign.services;

import java.io.IOException;
import java.nio.file.Path;
import java.util.Base64;
import java.util.List;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.eaisign.exceptions.UserNotFoundException;
import com.eaisign.models.Document;
import com.eaisign.models.Envoloppe;

@Service
public interface FileStorageService {
	
	//Envoloppes
	
	String CreateDirectory(String nom,Long id);
	Envoloppe save(List<MultipartFile> files,String nom,String status,Long id);
	 List<Envoloppe> getAllEnvoloppes(Long id) throws UserNotFoundException;
	 List<Envoloppe> getEnvoloppesByStatus(Long id,String status) throws UserNotFoundException;
	 
	//Documents
	 
	  void save(MultipartFile file,Long id ) ;
	  Resource load(String filename,Long id);
	  Stream<Path> loadAll();
	 
}
