package com.eaisign.services.implementations;

import com.eaisign.exceptions.UserNotFoundException;
import com.eaisign.models.Document;
import com.eaisign.models.Enveloppe;
import com.eaisign.models.User;
import com.eaisign.payload.request.DocumentReport;
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

import javax.activation.DataHandler;
import javax.activation.DataSource;
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

	public JasperPrint  exportReportEnveloppes( Long id,List<Enveloppe> enveloppes) throws FileNotFoundException, JRException {

		User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException());
//		List<User> users = new ArrayList<>();
//		users.add(user);
//		File fileUser = ResourceUtils.getFile("classpath:user.jrxml");
//		JasperReport jasperReportUser = JasperCompileManager.compileReport(fileUser.getAbsolutePath());
//		JRBeanCollectionDataSource dataSourceUser = new JRBeanCollectionDataSource(users);
//		Map<String, Object> parameters = new HashMap<>();
//		JasperPrint jasperPrintUser = JasperFillManager.fillReport(jasperReportUser, parameters, dataSourceUser);
//		JRBeanCollectionDataSource Dataset1 = new JRBeanCollectionDataSource(enveloppes);
		File fileDocuments = ResourceUtils.getFile("classpath:UserDocs.jrxml");

		File fileAdress = ResourceUtils.getFile("classpath:UserDocsSubreport.jrxml");




		JasperReport jasperReportDocuments = JasperCompileManager.compileReport(fileDocuments.getAbsolutePath());
		JasperReport jasperReportAdress = JasperCompileManager.compileReport(fileAdress.getAbsolutePath());

		JRBeanCollectionDataSource dataSourceDocs = new JRBeanCollectionDataSource(enveloppes);
		Map<String, Object> parametersDocs = new HashMap<>();
		parametersDocs.put("nom", user.getUsername());
		parametersDocs.put("prenom", user.getPrenom());
		parametersDocs.put("subreportParameter", jasperReportAdress);
		JasperPrint jasperPrintDocuments = JasperFillManager.fillReport(jasperReportDocuments, parametersDocs,
				dataSourceDocs);

		return jasperPrintDocuments;
	}
	public JasperPrint  exportReportDocuments(Long id, List<Enveloppe> enveloppes) throws FileNotFoundException, JRException {

		User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException());



		List<DocumentReport> documents=new ArrayList<>();
		for(Enveloppe enveloppe:enveloppes){
			List<Document> documents_=enveloppe.getDocuments();
			for(Document document:documents_){
				DocumentReport documentReport = new DocumentReport();
				documentReport.setStatus(enveloppe.getStatus());
				documentReport.setDateAjout(enveloppe.getDateAjout());
				documentReport.setDateDernierModification(enveloppe.getDerniereModification());
				documentReport.setNom(document.getNom());
				documentReport.setSignataire(document.getSignataire().getEmail());
				documents.add(documentReport);
			}
		}


		File fileDocuments = ResourceUtils.getFile("classpath:UserDocuments.jrxml");

		JasperReport jasperReportDocuments = JasperCompileManager.compileReport(fileDocuments.getAbsolutePath());
		JRBeanCollectionDataSource CollectionBeanParam = new JRBeanCollectionDataSource(documents);
		Map<String, Object> parametersDocs = new HashMap<>();
		parametersDocs.put("nom", user.getUsername());
		parametersDocs.put("prenom", user.getPrenom());
		parametersDocs.put("CollectionBeanParam",CollectionBeanParam);
		JasperPrint jasperPrintDocuments = JasperFillManager.fillReport(jasperReportDocuments, parametersDocs,new JREmptyDataSource());

		return jasperPrintDocuments;
	}

	public JasperPrint  exportUserReport( Long id) throws FileNotFoundException, JRException {
		User user = userRepository.findById(id).orElseThrow(() -> new UserNotFoundException());
		List<User> users = new ArrayList<>();
		users.add(user);
		File fileUser = ResourceUtils.getFile("classpath:user.jrxml");
		JasperReport jasperReportUser = JasperCompileManager.compileReport(fileUser.getAbsolutePath());
		JRBeanCollectionDataSource dataSourceUser = new JRBeanCollectionDataSource(users);
		Map<String, Object> parameters = new HashMap<>();
		JasperPrint jasperPrintUser = JasperFillManager.fillReport(jasperReportUser, parameters, dataSourceUser);
		return  jasperPrintUser;
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
		javaMailSender.send(message);
		return "Email Sent";

	}
	public String sendReportstoEmail(String toEmail, String subject, String body, HashMap<String,DataSource> attachments) {
		// SimpleMailMessage message=new SimpleMailMessage();
		MimeMessage message = javaMailSender.createMimeMessage();
		try {
			// String rootFile=root+"/"+multipartFile.getOriginalFilename();
			Address addressTo = new InternetAddress(toEmail);
			message.setSubject(subject);
			message.setRecipient(Message.RecipientType.TO, addressTo);
			MimeMultipart multipart = new MimeMultipart();
			MimeBodyPart messagebody = new MimeBodyPart();
			for(Map.Entry m : attachments.entrySet()){
				MimeBodyPart attachement = new MimeBodyPart();
				DataSource attachment= (DataSource) m.getValue();
				attachement.setDataHandler(new DataHandler(attachment));
				attachement.setDisposition(Part.ATTACHMENT);
				attachement.setFileName((String) m.getKey()+".pdf");
				multipart.addBodyPart(attachement);
			}


			messagebody.setText(body);
			multipart.addBodyPart(messagebody);
			message.setContent(multipart);
			javaMailSender.send(message);
		} catch (MessagingException e) {
			throw new RuntimeException(e);
		}
		javaMailSender.send(message);
		return "Email Sent";

	}
}
