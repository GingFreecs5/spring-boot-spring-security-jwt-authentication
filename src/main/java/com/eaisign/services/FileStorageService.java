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
import com.eaisign.models.User;

@Service
public interface FileStorageService {
	
	//Envoloppes
	
	String CreateDirectory(Long id);
	Envoloppe saveEnvoloppe(String nom,String status,User user);
	 List<Envoloppe> getAllEnvoloppes(Long id) throws UserNotFoundException;
	 List<Envoloppe> getEnvoloppesByStatus(Long id,String status) throws UserNotFoundException;
	 
	//Documents
	 
	 Document saveDocument(MultipartFile file,Long id ) ;
	 Document saveDocument(String nom,Envoloppe envoloppe);
	  Resource load(String filename,Long id);
	  Stream<Path> loadAll();
	 
}
