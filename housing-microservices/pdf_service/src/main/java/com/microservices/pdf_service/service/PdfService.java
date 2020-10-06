package com.microservices.pdf_service.service;

import com.microservices.pdf_service.model.Application;
import com.microservices.pdf_service.model.Bill;
import com.microservices.pdf_service.model.Consumption;

import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

public interface PdfService {
    byte[] exportBill(Bill bill, HttpServletResponse response, Locale locale);
    void exportApplicationForm(Application applicationForm, HttpServletResponse response, Locale locale);
}
