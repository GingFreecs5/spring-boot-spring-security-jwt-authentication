package com.eaisign.payload.request;

import lombok.Data;

import java.util.List;

@Data
public class ReportEmailRequest {
    private List<ReportRequest> reports;
}
