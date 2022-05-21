package com.eaisign.services.implementations;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.DirectoryNotEmptyException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.sql.Date;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.eaisign.exceptions.UserNotFoundException;
import com.eaisign.models.Document;
import com.eaisign.models.Envoloppe;
import com.eaisign.models.User;
import com.eaisign.repository.DocumentRepository;
import com.eaisign.repository.EnvoloppeRepository;
import com.eaisign.repository.UserRepository;
import com.eaisign.services.FileStorageService;

@Service
public class FileStorageServiceImp implements FileStorageService {

	@Autowired
	private EnvoloppeRepository envoloppeRepo;
	@Autowired
	private UserRepository userRepo;
	@Autowired 
	private DocumentRepository documentRepo;
	static final String ROOT = "C:/Users/yassi/OneDrive/Documents/EAI_Docs/";
//	static final String ROOT = "C:/Users/akkabyas/Documents/EaiDocs/";
	@Override
	public String CreateDirectory( Long id) {

		if (new File(ROOT + id).mkdirs()) {
			return "Folder created";
		} else {
			return "Folder not created";
		}

	}

	@Override
	public Envoloppe saveEnvoloppe( String nom, String status, User user) {
			Envoloppe env=new Envoloppe(nom,status,user);
			try {
				return envoloppeRepo.save(env);
			}catch(Exception e) {
				throw new
				  RuntimeException("Envoloppe not saved : " + e.getMessage());
			}
	}

	@Override
	public List<Envoloppe> getAllEnvoloppes(Long id) throws UserNotFoundException {
		User user = userRepo.findById(id).orElse(null);
		if (user == null) {
			throw new UserNotFoundException();
		} else {
			List<Envoloppe> envoloppes = envoloppeRepo.findByUser(user);
			return envoloppes;
		}

	}

	@Override
	public List<Envoloppe> getEnvoloppesByStatus(Long id, String status) throws UserNotFoundException {
		// TODO Auto-generated method stub
		User user = userRepo.findById(id).orElse(null);
		if (user == null) {
			throw new UserNotFoundException();

		} else {
			List<Envoloppe> envoloppes = envoloppeRepo.findByStatusAndUser(status, user);
			return envoloppes;

		}
	}

	@Override
	public Document saveDocument(MultipartFile file, Long id) {

		
		  String root = ROOT + id; 
		  Path path = Paths.get(root);
		  Document document=new Document(file.getOriginalFilename());
		  try {
		  Files.copy(file.getInputStream(), path.resolve(file.getOriginalFilename()),StandardCopyOption.REPLACE_EXISTING);
		  return document;
		  } catch (Exception e) { throw new
		  RuntimeException("Could not store the file. Error: " + e.getMessage()); }
		 

	}
	
	

	@Override
	public Resource load(String filename, Long id) {
		
//		  try { root = ROOT + root; Path path = Paths.get(root); Path file =
//		  path.resolve(filename); Resource resource = new UrlResource(file.toUri()); if
//		  (resource.exists() || resource.isReadable()) { return resource; } else {
//		  throw new RuntimeException("Could not read the file!"); } } catch
//		  (MalformedURLException e) { throw new RuntimeException("Error: " +
//		  e.getMessage()); }
		 
		return null;
	}

	@Override
	public Stream<Path> loadAll() {
		/*
		 * try { return Files.walk(this.root, 1).filter(path ->
		 * !path.equals(this.root)).map(this.root::relativize); } catch (IOException e)
		 * { throw new RuntimeException("Could not load the files!"); }
		 */
		return null;
	}

	@Override
	public Document saveDocument(String nom, Envoloppe envoloppe) {
		Document document = new Document(nom,envoloppe);
		return documentRepo.save(document);
	}

	@Override
	public String deleteFile(String fileName,Long id) {
		
		String msg="";
		 String root = ROOT + id + "/"+fileName; 
		 System.out.println(root);
		  Path path = Paths.get(root);
		 try {
	            Files.deleteIfExists(path);
	            System.out.println(Files.deleteIfExists(path));
	            msg="File deleted";
	        
	        }
	        catch (NoSuchFileException e) {
	        
	            msg="No such file/directory exists";
	            
	                   }
	        catch (DirectoryNotEmptyException e) {
	            msg="Directory is not empty.";
	          
	        }
	        catch (IOException e) {
	            msg="Directory is not empty.";
	         
	        }
		    return msg;
	   
	    }
		
	}


