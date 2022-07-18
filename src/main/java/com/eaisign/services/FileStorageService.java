package com.eaisign.services;

import java.io.IOException;
import java.nio.file.NoSuchFileException;
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
import com.eaisign.models.Enveloppe;
import com.eaisign.models.User;

@Service
public interface FileStorageService {
	
	//Envelopes
	
	String CreateDirectory(Long id);
	Enveloppe saveEnveloppe(String nom,String status,User user);
	Enveloppe getEnveloppe(Long id);
	 List<Enveloppe> getAllEnveloppes(Long id) throws UserNotFoundException;
	 List<Enveloppe> getEnveloppesByStatus(Long id,String status) throws UserNotFoundException;
	 
	//Documents
	 String uploadFile(MultipartFile file,Long id ) ;
	 Document saveDocument(String nom,Enveloppe envoloppe);
	  Resource load(String filename,Long id);
	  Stream<Path> loadAll();
	 String deleteFile(String fileName,Long id) throws IOException;
}
