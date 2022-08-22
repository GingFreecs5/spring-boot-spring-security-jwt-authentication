package com.eaisign.payload.request;

import lombok.Data;

@Data
public class ReportRequest {
    private String status;
    private String date;
    private String type;
    private boolean selected;
    private String reportName;

}
