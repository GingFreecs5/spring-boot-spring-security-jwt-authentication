package com.eaisign.services.implementations;

import com.eaisign.exceptions.UserNotFoundException;
import com.eaisign.models.Document;
import com.eaisign.models.Enveloppe;
import com.eaisign.models.User;
import com.eaisign.repository.DocumentRepository;
import com.eaisign.repository.EnveloppeRepository;
import com.eaisign.repository.UserRepository;
import lombok.AllArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.mail.*;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

	@Autowired
	private JavaMailSender javaMailSender;
	private DocumentRepository documentRepository;
	private UserRepository userRepository;
	private EnveloppeRepository enveloppeRepository;

	public ReportService(DocumentRepository documentRepository, UserRepository userRepository,
			EnveloppeRepository enveloppeRepository) {
		this.documentRepository = documentRepository;
		this.userRepository = userRepository;
		this.enveloppeRepository = enveloppeRepository;
	}

	public String exportReport( String status,String date, Long id) throws FileNotFoundException, JRException {

		User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException());
		System.out.println(status);
		List<Enveloppe> enveloppes_=new ArrayList<Enveloppe>();
		if(status.equals("Terminé")) {
			enveloppes_ = enveloppeRepository.findByStatusAndUser(status,user);

		}else if (status.equals("All")) {
			 enveloppes_ = enveloppeRepository.findByUser(user);


		}else {
			enveloppes_=null;
		}
		List<Enveloppe> enveloppes=new ArrayList<Enveloppe>();
		if(date.equals("Day")) {
			Date yesterday=new Date(System.currentTimeMillis()-24*60*60*1000);
			System.out.println(yesterday);
			for(Enveloppe enveloppe:enveloppes_){
				if(enveloppe.getDerniereModification().after(yesterday)){
					System.out.println(enveloppe.getDerniereModification());
					enveloppes.add(enveloppe);
				}
			}
		}else if(date.equals("Week")) {
			Date lastweek=	new Date(System.currentTimeMillis()-7*24*60*60*1000);
			for(Enveloppe enveloppe:enveloppes_){
				if(enveloppe.getDerniereModification().after(lastweek)){
					System.out.println(enveloppe.getDerniereModification());
					enveloppes.add(enveloppe);
				}
			}

		}else if(date.equals("Month")) {
			Date lastmonth=	new Date(System.currentTimeMillis()-24*24*60*60*1000);
			for(Enveloppe enveloppe:enveloppes_){
				if(enveloppe.getDerniereModification().after(lastmonth)){

					enveloppes.add(enveloppe);
				}
			}
		}
		
		
		List<User> users = new ArrayList<>();
		users.add(user);
		String path = "D:\\JasperDocuments\\";
		File fileDocuments = ResourceUtils.getFile("classpath:UserDocs.jrxml");
		File fileUser = ResourceUtils.getFile("classpath:user.jrxml");
		File fileAdress = ResourceUtils.getFile("classpath:UserDocsSubreport.jrxml");

		JasperReport jasperReportUser = JasperCompileManager.compileReport(fileUser.getAbsolutePath());
		JRBeanCollectionDataSource dataSourceUser = new JRBeanCollectionDataSource(users);
		Map<String, Object> parameters = new HashMap<>();
		parameters.put("createdBy", "ME");
		JasperPrint jasperPrintUser = JasperFillManager.fillReport(jasperReportUser, parameters, dataSourceUser);

		JasperReport jasperReportDocuments = JasperCompileManager.compileReport(fileDocuments.getAbsolutePath());
		JasperReport jasperReportAdress = JasperCompileManager.compileReport(fileAdress.getAbsolutePath());
		JRBeanCollectionDataSource Dataset1 = new JRBeanCollectionDataSource(enveloppes);
		JRBeanCollectionDataSource dataSourceDocs = new JRBeanCollectionDataSource(enveloppes);
		Map<String, Object> parametersDocs = new HashMap<>();
		parametersDocs.put("nom", user.getUsername());
		parametersDocs.put("prenom", user.getPrenom());
		parametersDocs.put("subreportParameter", jasperReportAdress);
		;
		JasperPrint jasperPrintDocuments = JasperFillManager.fillReport(jasperReportDocuments, parametersDocs,
				dataSourceDocs);
		String userFilename = user.getPieceJusticatif() + "User";
		String documentsFilename = user.getPieceJusticatif() + "Documents";
		userFilename = userFilename + ".pdf";
		documentsFilename = documentsFilename + ".pdf";
		JasperExportManager.exportReportToPdfFile(jasperPrintUser, path + userFilename);
		JasperExportManager.exportReportToPdfFile(jasperPrintDocuments, path + documentsFilename);
		return "Rapport généré";
	}

	public String sendEmail(String toEmail, String subject, String body, String root, MultipartFile[] multipartFiles) {
		// SimpleMailMessage message=new SimpleMailMessage();
		MimeMessage message = javaMailSender.createMimeMessage();
		try {
			// String rootFile=root+"/"+multipartFile.getOriginalFilename();
			Address addressTo = new InternetAddress(toEmail);
			message.setSubject(subject);
			message.setRecipient(Message.RecipientType.TO, addressTo);
			MimeMultipart multipart = new MimeMultipart();
			MimeBodyPart messagebody = new MimeBodyPart();
			for (MultipartFile multipartFile : multipartFiles) {
				MimeBodyPart attachement = new MimeBodyPart();
				attachement.setContent(multipartFile.getBytes(), multipartFile.getContentType());
				attachement.setFileName(multipartFile.getOriginalFilename());
				attachement.setDisposition(Part.ATTACHMENT);
				multipart.addBodyPart(attachement);
			}
			messagebody.setText(body);
			multipart.addBodyPart(messagebody);
			message.setContent(multipart);
			javaMailSender.send(message);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		} catch (IOException e) {
			throw new RuntimeException(e);
		}

//        message.setFrom("yassinakkab@gmail.com");
//        message.setTo(toEmail);
//        message.setText(body);
//        message.setSubject(subject);

		javaMailSender.send(message);
		return "Email Sent";

	}

}
