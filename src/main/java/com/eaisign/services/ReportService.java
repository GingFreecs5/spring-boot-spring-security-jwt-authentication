package com.eaisign.services;

import com.eaisign.models.Document;
import com.eaisign.repository.DocumentRepository;
import lombok.AllArgsConstructor;
import net.sf.jasperreports.engine.*;
import net.sf.jasperreports.engine.data.JRBeanCollectionDataSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.ResourceUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class ReportService {

    private DocumentRepository documentRepository;

    public ReportService(DocumentRepository documentRepository) {
        this.documentRepository = documentRepository;
    }

    public String exportReport(String reportFormat,String nom) throws FileNotFoundException, JRException {
        List<Document> documents=documentRepository.findAll();

        String path="D:\\JasperDocuments";
        File file= ResourceUtils.getFile("classpath:documents.jrxml");
        JasperReport jasperReport = JasperCompileManager.compileReport(file.getAbsolutePath());
        JRBeanCollectionDataSource dataSource=new JRBeanCollectionDataSource(documents);
        Map<String,Object> parameters=new HashMap<>();
        parameters.put("createdBy","ME");
        JasperPrint jasperPrint = JasperFillManager.fillReport(jasperReport,parameters,dataSource);
        if(reportFormat.equalsIgnoreCase("html")){
            JasperExportManager.exportReportToHtmlFile(jasperPrint,path+nom );
        }
        if(reportFormat.equalsIgnoreCase("pdf")){
            JasperExportManager.exportReportToPdfFile(jasperPrint,path+nom  );

        }
        return path;
    }

}
