package com.eaisign.controllers;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import com.eaisign.payload.request.*;
import com.eaisign.repository.DocumentRepository;
import com.eaisign.services.implementations.ReportService;
import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.JasperReport;
import oracle.jdbc.proxy.annotation.Post;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import com.eaisign.exceptions.UserNotFoundException;
import com.eaisign.models.Document;
import com.eaisign.models.Enveloppe;
import com.eaisign.models.Signataire;
import com.eaisign.models.User;
import com.eaisign.payload.message.FilesbyEnveloppeIdResponse;
import com.eaisign.payload.message.ResponseMessage;
import com.eaisign.payload.response.File64Response;
import com.eaisign.repository.UserRepository;
import com.eaisign.security.services.UserDetailsImpl;
import com.eaisign.services.FileStorageService;
import com.eaisign.services.implementations.UserServiceImp;

@Controller

@RequestMapping("/api/files")
@CrossOrigin(origins = "http://localhost:3000", maxAge = 3600)
public class FileController {

	static final String ROOT = "C:/Users/yassi/OneDrive/Documents/EAI_Docs/";
	static final String ENVROOT= "Enveloppes/";
	static final String REPORTROOT= "Rapports/";


	private FileStorageService fileStorageService;
	private ReportService reportService;

	private DocumentRepository documentRepository;
	private UserRepository userRepository;

	private UserServiceImp userServiceImp;


	public FileController(FileStorageService fileStorageService,ReportService reportService, DocumentRepository documentRepository, UserRepository userRepository, UserServiceImp userServiceImp) {
		this.fileStorageService = fileStorageService;
		this.documentRepository = documentRepository;
		this.userRepository = userRepository;
		this.userServiceImp = userServiceImp;
		this.reportService=reportService;
	}
	


	/***************************************
	 * Upload File
	 ***************************************/

	@PostMapping("/uploadfile")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<File64Response> uploadFile(@RequestParam("file") MultipartFile file) {
		String message = "";
		UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		File64Response file64Response = new File64Response("", "");
		String file64 = fileStorageService.uploadFile(file, ROOT + user.getId()+"/"+ENVROOT);
		file64Response.setName(file.getOriginalFilename());
		file64Response.setPdfB64(file64);
		message = "Uploaded the file successfully: " + file.getOriginalFilename();
		return ResponseEntity.status(HttpStatus.OK).body(file64Response);

	}

	/***************************************
	 * Get Files by EnveloppeId
	 ***************************************/
	@PostMapping("/files/{envId}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<List<FilesbyEnveloppeIdResponse>> getFilesbyEnveloppeId(@PathVariable("envId") Long envId) {
		List<FilesbyEnveloppeIdResponse> filesbyEnveloppeIdResponses = new ArrayList<FilesbyEnveloppeIdResponse>();
		UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		String path = ROOT + user.getId() + "/" +ENVROOT+ envId;
		System.out.println(path);
		File[] files = fileStorageService.getFilesbyEnvid(path);
		if (files == null) {

			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(filesbyEnveloppeIdResponses);

		} else {
			for (File file : files) {
				filesbyEnveloppeIdResponses.add(new FilesbyEnveloppeIdResponse(file.getName(), false));
			}
			return ResponseEntity.status(HttpStatus.OK).body(filesbyEnveloppeIdResponses);
		}

	}

	/************************************
	 * Upload Files
	 ***************************************/

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

	/**************************************
	 * Save Enveloppe
	 ****************************************/

	@PostMapping("/saveEnveloppe")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<?> SaveEnveloppe(@RequestBody NewEnvRequest request) {
		String message = "";
		UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		try {
			User user_ = userServiceImp.findUser(user.getId());
			try {
				Enveloppe enveloppe = fileStorageService.saveEnveloppe(request.getNom(), request.getStatus(),
						request.getFavoris(), user_);
				message = "Envelope sauvegardé";
				System.out.println(enveloppe);
				return ResponseEntity.status(HttpStatus.OK).body(enveloppe);
			} catch (Exception e) {
				message = "Error";
				return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));
			}
		} catch (UserNotFoundException e1) {
			message = "User Not Found";
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(message));

		}
	}

	@PutMapping("/updateEnveloppe/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<?> UpdateEnveloppe(@PathVariable Long id, @RequestBody NewEnvRequest request) {
		Enveloppe enveloppe = fileStorageService.getEnveloppe(id);
		enveloppe.setNom(request.getNom());
		enveloppe.setStatus(request.getStatus());
		enveloppe.setFavoris(request.getFavoris());
		return ResponseEntity.status(HttpStatus.OK).body(fileStorageService.saveEnveloppe(enveloppe));
	}

	/**************************************
	 * Save Documents
	 *************************************/

	@PostMapping("/saveDocuments")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<?> SaveDocuments(@RequestBody NewDocRequest request) {
		boolean fileMoved = false;
		UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		fileStorageService.CreateDirectory(ROOT + user.getId() + "/"+ENVROOT + request.getIdEnveloppe());

		String message = "Files saved";
		Enveloppe enveloppe = fileStorageService.getEnveloppe(request.getIdEnveloppe());
		Signataire signataire = fileStorageService.saveSignataire(request.getEmail());
		List<Signataire> signataires=new ArrayList<>();
		for (String file : request.getFiles()) {
			Document document = fileStorageService.saveDocument(file, enveloppe, request.getCanalUtilise(), signataire);
			if(request.isCopyFiles()) {
			fileMoved = fileStorageService.copyFile(ROOT + user.getId() + "/" +ENVROOT+ file,
					ROOT + user.getId() + "/"  +ENVROOT+ request.getIdEnveloppe()+ "/" + file);
			if (!fileMoved)
				break;
			}else {
				return ResponseEntity.status(HttpStatus.OK).body(message);

			}
		}
		if (fileMoved) {
			return ResponseEntity.status(HttpStatus.OK).body(message);

		} else {
			message = "Documents pas enregistrés";

			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(message);

		}

	}

	/********************************
	 * Delete File
	 ************************************/

	@PostMapping("/delete/{name}/{isdocdeleted}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<ResponseMessage> deleteFile(@PathVariable("name") String name,
			@PathVariable("isdocdeleted") boolean isdocdeleted) {
		String msg;
		UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		try {
			msg = fileStorageService.deleteFile(name, ROOT + user.getId()+"/"+ENVROOT);
			if (isdocdeleted) {
				fileStorageService.deleteDocument(null);
			}
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(msg));
		} catch (IOException e) {

			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(e.getMessage()));
		}

	}

	/************************************
	 * Delete Files
	 ***************************************/

	@PostMapping("/delete/files")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<ResponseMessage> deleteFiles(@RequestBody String[] files) {
		String msg;
		UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		try {
			for (String filename : files) {
				fileStorageService.deleteFile(filename, ROOT + user.getId()+"/"+ENVROOT);
			}
			msg = "files Deleted";
			return ResponseEntity.status(HttpStatus.OK).body(new ResponseMessage(msg));
		} catch (IOException e) {

			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(e.getMessage()));
		}
	}

	/************************************
	 * Get Enveloppes
	 ***************************************/

	@GetMapping("/enveloppes")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<?> GetEnveloppes() {
		String msg;
		UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();

		try {
			List<Enveloppe> enveloppes = fileStorageService.getAllEnveloppes(user.getId());
			return ResponseEntity.status(HttpStatus.OK).body(enveloppes);

		} catch (UserNotFoundException e) {
			return ResponseEntity.status(HttpStatus.EXPECTATION_FAILED).body(new ResponseMessage(e.getMessage()));

		}
	}

	/************************************
	 * Delete Enveloppe
	 ***************************************/
	@DeleteMapping("/delete/enveloppe/{envId}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<?> DeleteEnveloppe(@PathVariable("envId") Long envId) {
		fileStorageService.deleteEnveloppe(envId);
		return ResponseEntity.status(HttpStatus.OK).body(envId);
	}

	/************************************
	 * Get Documents
	 ***************************************/

	@GetMapping("/documents/{id}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<?> GetDocuments(@PathVariable("id") Long id ) {
		List<Document> enveloppes = fileStorageService.getDocumentsbyEnveloppeId(id);
		return ResponseEntity.status(HttpStatus.OK).body(enveloppes);

	}
	/*********************
	 * Delete Documents by EnvID
	 */
	@DeleteMapping("/delete/documents/{envId}")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<?> DeleteDocumentsbyEnvId(@PathVariable("envId") Long envId){
		List<Document> documents=fileStorageService.getDocumentsbyEnveloppeId(envId);
		for(Document document:documents){
			documentRepository.delete(document);
		}
		return ResponseEntity.status(HttpStatus.OK).body("Documents Supprimés");
	}

	/***********************
	* Génération des rapports
	 ****************************/
	@PostMapping("/exportEnveloppes")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<?> GenerateReportEnveloppes(@RequestBody ReportRequest request) throws JRException, FileNotFoundException {
		UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		fileStorageService.CreateDirectory(ROOT + user.getId() + "/"+REPORTROOT);
		String reportName=request.getReportName();
		reportName=reportName.replaceAll("\\s+","");
		HttpHeaders headers = new HttpHeaders();
		//set the PDF format
		headers.setContentType(MediaType.APPLICATION_PDF);

		headers.setContentDispositionFormData("filename", reportName+".pdf");
		String path=ROOT + user.getId() + "/"+REPORTROOT+"/" +reportName +".pdf";
		JasperPrint response =reportService.exportReportEnveloppes(request.getStatus(),request.getDate(),user.getId(),path);

		return new ResponseEntity<byte[]>(JasperExportManager.exportReportToPdf(response),headers,HttpStatus.OK);
	}

	@PostMapping("/exportDocuments")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<byte[]> GenerateReportDocuments(@RequestBody ReportRequest request) throws JRException, FileNotFoundException {
		UserDetailsImpl user = (UserDetailsImpl) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		fileStorageService.CreateDirectory(ROOT + user.getId() + "/"+REPORTROOT);
		String reportName=request.getReportName();
		reportName=reportName.replaceAll("\\s+","");
		HttpHeaders headers = new HttpHeaders();
		String path=ROOT + user.getId() + "/"+REPORTROOT+"/" +reportName +".pdf";

		//set the PDF format
		headers.setContentType(MediaType.APPLICATION_PDF);
		headers.setContentDispositionFormData("filename", reportName+".pdf");
		JasperPrint response =reportService.exportReportDocuments(request.getStatus(),request.getDate(),user.getId(),path);

		return new ResponseEntity<byte[]>(JasperExportManager.exportReportToPdf(response),headers,HttpStatus.OK);
	}
	/***********************
	 Envoi d'email
	 ****************************/
	@PostMapping("/sendMail")
	@PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
	public ResponseEntity<?> SendEmail(@ModelAttribute MailRequest request){
		String response=reportService.sendEmail(request.getToEmail(), request.getSubject(), request.getBody(), ROOT+"Mail/",request.getFiles());
		return ResponseEntity.status(HttpStatus.OK).body(response);

	}
}