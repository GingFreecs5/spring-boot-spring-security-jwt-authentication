package com.eaisign.services.implementations;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Date;
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
import com.eaisign.repository.EnvoloppeRepository;
import com.eaisign.repository.UserRepository;
import com.eaisign.services.FileStorageService;

@Service
public class FileStorageServiceImp implements FileStorageService {

	@Autowired
	private EnvoloppeRepository envoloppeRepo;
	@Autowired
	private UserRepository userRepo;
	static final String ROOT = "C:/Users/akkabyas/Documents/EaiDocs/";

	@Override
	public String CreateDirectory(String nom, Long id) {

		if (new File(ROOT + nom + id).mkdirs()) {
			return "Folder created";
		} else {
			return "Folder not created";
		}

	}

	@Override
	public Envoloppe save(List<MultipartFile> files, String nom, String status, Long id) {
		
		List<Document> documents = files.stream().map(file -> {
			this.save(file, id);
			Document doc = new Document();
			doc.setNom(file.getOriginalFilename());
			return doc;

		}).collect(Collectors.toList());

		Envoloppe envoloppe = new Envoloppe(nom, status, documents);
		return envoloppeRepo.save(envoloppe);
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
	public void save(MultipartFile file, Long id) {

		/*
		 * root = ROOT + root; Path path = Paths.get(root); try {
		 * Files.copy(file.getInputStream(), path.resolve(file.getOriginalFilename()));
		 * } catch (Exception e) { throw new
		 * RuntimeException("Could not store the file. Error: " + e.getMessage()); }
		 */

	}

	@Override
	public Resource load(String filename, Long id) {
		/*
		 * try { root = ROOT + root; Path path = Paths.get(root); Path file =
		 * path.resolve(filename); Resource resource = new UrlResource(file.toUri()); if
		 * (resource.exists() || resource.isReadable()) { return resource; } else {
		 * throw new RuntimeException("Could not read the file!"); } } catch
		 * (MalformedURLException e) { throw new RuntimeException("Error: " +
		 * e.getMessage()); }
		 */
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

}
