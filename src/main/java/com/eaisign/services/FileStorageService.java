package com.eaisign.services;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.util.Base64;
import java.util.List;
import java.util.stream.Stream;

import net.sf.jasperreports.engine.JRException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import com.eaisign.exceptions.EnveloppeNotFoundException;
import com.eaisign.exceptions.UserNotFoundException;
import com.eaisign.models.Document;
import com.eaisign.models.Enveloppe;
import com.eaisign.models.Signataire;
import com.eaisign.models.User;

@Service
public interface FileStorageService {
	
	/*****************************Envelopes***************************/

  	 Enveloppe saveEnveloppe(String nom,String status,Boolean favoris,User user);
  	 Enveloppe saveEnveloppe(Enveloppe enveloppe);
	 Enveloppe getEnveloppe(Long id) throws EnveloppeNotFoundException;
	 List<Enveloppe> getAllEnveloppes(Long id) throws UserNotFoundException;
	 List<Enveloppe> getEnveloppesByStatus(Long id,String status) throws UserNotFoundException;


	 void deleteEnveloppe(Long envId) ;


	/*****************************************Documents***********************************/


	 Document saveDocument(String nom,Enveloppe envoloppe,String canalUtilise,Signataire signataire);
	 List<Document> getDocumentsbyEnveloppeId(Long id);
	 void deleteDocument(Long id);

	 String deleteDocumentsbyEnvid(Long envId);

	 
	 /*******************************************Files************************************/



	 String CreateDirectory(String root);
	 String uploadFile(MultipartFile file,String root );
	 boolean copyFile(String sourcePath,String targetPath);
	 String deleteFile(String fileName,String root) throws IOException;
	 File[] getFilesbyEnvid(String root);
	 //Signataires
	 Signataire saveSignataire(String email);


}
