/*
package com.jee.spring_labs.user.service.impl;

import com.itextpdf.text.Chunk;
import com.itextpdf.text.Document;
import com.itextpdf.text.DocumentException;
import com.itextpdf.text.Paragraph;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.jee.spring_labs.owner.dao.BillRepository;
import com.jee.spring_labs.owner.model.Bill;
import com.jee.spring_labs.tenant.model.Consumption;
import com.jee.spring_labs.user.service.PdfService;
import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Locale;

@Service
public class PdfServiceImpl implements PdfService {

    @Autowired
    private BillRepository dao;

    @Autowired
    private MessageSource messageSource;

    @Override
    public void export(Bill bill, HttpServletResponse response, Locale locale) {
        try {
            OutputStream os = response.getOutputStream();

            response.setCharacterEncoding("UTF-8");
            response.setContentType("application/pdf");
            response.setHeader("Content-Disposition",
                    "inline; filename=bill_" + bill.getId() + ".pdf");

            Document pdf = new Document();
            PdfWriter.getInstance(pdf, os);
            pdf.open();

            String buildingName = bill.getApartment().getBuilding().getName();
            int roomNumber = bill.getApartment().getRoomNumber();

            pdf.add(new Paragraph(messageSource.getMessage("pdf.title", new String[]{bill.getDateOfCreation().toString(), bill.getOwner().getUsername()}, locale)));
            pdf.add(new Paragraph(Chunk.NEWLINE));

            PdfPTable table = new PdfPTable(2);
            table.addCell(messageSource.getMessage("pdf.accepted", null, locale));
            table.addCell(((Boolean) bill.isAccepted()).toString());
            table.addCell(messageSource.getMessage("pdf.building", null, locale));
            table.addCell(buildingName);
            table.addCell(messageSource.getMessage("pdf.room", null, locale));
            table.addCell(((Integer) roomNumber).toString());
            table.addCell(messageSource.getMessage("pdf.date.creation", null, locale));
            table.addCell(bill.getDateOfCreation().toString());
            table.addCell(messageSource.getMessage("pdf.date.payment", null, locale));
            String paymentDate = (bill.getDateOfPayment() != null) ? bill.getDateOfPayment().toString() : "";
            table.addCell(paymentDate);
            table.addCell(messageSource.getMessage("pdf.cost", null, locale));
            table.addCell(((Double) bill.getCost()).toString() + " zl");
            pdf.add(table);
            pdf.add(new Paragraph(Chunk.NEWLINE));

            pdf.add(new Paragraph(messageSource.getMessage("pdf.consumptions", null, locale)));
            pdf.add(new Paragraph(Chunk.NEWLINE));
            PdfPTable table2 = new PdfPTable(3);

            for (Consumption consumption : bill.getConsumptions()) {
                table2.addCell(messageSource.getMessage("label.consumption." + consumption.getType().toString().toLowerCase(), null, locale));
                table2.addCell(((Double) consumption.getAmount()).toString() + " " + consumption.getType().getUnit().replace("<sup>", "").replace("</sup>", ""));
                table2.addCell(((Double) consumption.getCost()).toString() + " zl");
            }

            pdf.add(table2);
            pdf.close();
            os.close();

        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }

    @Override
    @Transactional
    public Bill findBillById(long id) {
        Bill bill = dao.findByIdAndRemovedIsFalse(id);
        Hibernate.initialize(bill.getConsumptions());
        return bill;
    }
}
*/
