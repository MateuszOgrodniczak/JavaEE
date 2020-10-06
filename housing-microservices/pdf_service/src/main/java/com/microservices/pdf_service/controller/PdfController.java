package com.microservices.pdf_service.controller;

import com.microservices.pdf_service.model.Bill;
import com.microservices.pdf_service.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

@RestController
@RequestMapping("/pdf")
public class PdfController {

    @Autowired
    private PdfService service;

    @GetMapping("/test")
    public String test() {
        return "PDF SERVICE";
    }

    @PostMapping("/exportBill")
    public ResponseEntity<byte[]> exportBill(@RequestBody Bill bill, HttpServletResponse response, Locale locale) {
        byte[] pdfAsBytes = service.exportBill(bill, response, locale);
        return ResponseEntity.ok().body(pdfAsBytes);
    }
}
