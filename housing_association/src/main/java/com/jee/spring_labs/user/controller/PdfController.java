package com.jee.spring_labs.user.controller;

import com.jee.spring_labs.user.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletResponse;
import java.util.Locale;

@Controller
public class PdfController {

    @Autowired
    private PdfService service;

    @GetMapping("/exportToPdf/{billId}")
    public void exportToPdf(@PathVariable("billId") long billId, HttpServletResponse response, Locale locale) {
        service.export(service.findBillById(billId), response, locale);
    }
}
