package com.microservices.pdf_service.service.impl;

import com.itextpdf.text.*;
import com.itextpdf.text.pdf.PdfPCell;
import com.itextpdf.text.pdf.PdfPTable;
import com.itextpdf.text.pdf.PdfWriter;
import com.microservices.pdf_service.model.Application;
import com.microservices.pdf_service.model.Bill;
import com.microservices.pdf_service.model.Consumption;
import com.microservices.pdf_service.service.PdfService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

@Service
public class PdfServiceImpl implements PdfService {

    @Autowired
    private MessageSource messageSource;

    @Override
    public byte[] exportBill(Bill bill, HttpServletResponse response, Locale locale) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try {
            Document pdf = new Document();
            PdfWriter.getInstance(pdf, byteArrayOutputStream);
            pdf.open();

            String buildingName = bill.getBuildingName();
            int roomNumber = bill.getRoomNumber();

            pdf.add(new Paragraph(messageSource.getMessage("pdf.bill.title", new String[]{bill.getDateOfCreation().toString(), bill.getIssuer()}, locale)));
            pdf.add(new Paragraph(Chunk.NEWLINE));

            PdfPTable table = new PdfPTable(2);
            table.addCell(messageSource.getMessage("pdf.bill.accepted", null, locale));
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
                table2.addCell(messageSource.getMessage("label.consumption." + consumption.getType().toLowerCase(), null, locale));
                table2.addCell(((Double) consumption.getAmount()).toString() + " " + consumption.getUnit().replace("<sup>", "").replace("</sup>", ""));
                table2.addCell(((Double) consumption.getCost()).toString() + " zl");
            }

            pdf.add(table2);
            pdf.close();

        } catch (DocumentException e) {
            e.printStackTrace();
        }
        return byteArrayOutputStream.toByteArray();
    }

    @Override
    public void exportApplicationForm(Application applicationForm, HttpServletResponse response, Locale locale) {
        try {
            String fileName = messageSource.getMessage("pdf.application", null, locale) + applicationForm.getId() + ".pdf";
            OutputStream os = response.getOutputStream();
            Document pdf = new Document();

            preparePdf(response, fileName, os, pdf);

            prepareSenderAndDateFields(pdf, locale, applicationForm.getIssuerNameAndSurname(), applicationForm.getCreationDate());
            prepareAddresseField(pdf, locale, applicationForm.getRecipients());

            prepareTitleAndBody(applicationForm, pdf);

            prepareSignatureField(pdf, locale, applicationForm.getIssuerNameAndSurname());

            pdf.close();
            os.close();
        } catch (IOException | DocumentException e) {
            e.printStackTrace();
        }
    }

    private void prepareTitleAndBody(Application applicationForm, Document pdf) throws DocumentException {
        PdfPTable titleFormTable = new PdfPTable(1);
        titleFormTable.setWidthPercentage(100);
        titleFormTable.addCell(getTitleCell(applicationForm.getTitle()));
        pdf.add(titleFormTable);
        pdf.add(new Paragraph(Chunk.NEWLINE));

        pdf.add(new Paragraph(applicationForm.getBody()));
        pdf.add(new Paragraph(Chunk.NEWLINE));
    }

    private void prepareSignatureField(Document pdf, Locale locale, String senderCredentials) throws DocumentException {
        PdfPTable senderSignatureFormTable = new PdfPTable(1);
        senderSignatureFormTable.setWidthPercentage(100);
        senderSignatureFormTable.addCell(getCell(messageSource.getMessage("pdf.signature", new String[]{senderCredentials}, locale), PdfPCell.ALIGN_RIGHT));
        pdf.add(new Paragraph(Chunk.NEWLINE));
        pdf.add(senderSignatureFormTable);
    }

    private void prepareAddresseField(Document pdf, Locale locale, List<String> recipientsNamesAndSurnames) throws DocumentException {
        PdfPTable addresseFormTable = new PdfPTable(1);
        addresseFormTable.setWidthPercentage(100);
        addresseFormTable.addCell(getCell(messageSource.getMessage("pdf.recipients", null, locale), PdfPCell.ALIGN_RIGHT));
        for (String recipient : recipientsNamesAndSurnames) {
            addresseFormTable.addCell(getCell(recipient, PdfPCell.ALIGN_RIGHT));
        }
        pdf.add(addresseFormTable);
        pdf.add(new Paragraph(Chunk.NEWLINE));
    }

    private void prepareSenderAndDateFields(Document pdf, Locale locale, String senderCredentials, LocalDate date) throws DocumentException {
        PdfPTable senderAndDateFormTable = new PdfPTable(2);
        senderAndDateFormTable.setWidthPercentage(100);
        senderAndDateFormTable.addCell(getCell(senderCredentials, PdfPCell.ALIGN_LEFT));
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        senderAndDateFormTable.addCell(getCell(messageSource.getMessage("pdf.date", new String[]{date.format(formatter)}, locale), PdfPCell.ALIGN_RIGHT));
        pdf.add(senderAndDateFormTable);
        pdf.add(new Paragraph(Chunk.NEWLINE));
    }

    private void preparePdf(HttpServletResponse response, String fileName, OutputStream os, Document pdf) throws DocumentException {
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/pdf");
        response.setHeader("Content-Disposition",
                "inline; filename=" + fileName);

        PdfWriter.getInstance(pdf, os);
        pdf.open();
    }

    private PdfPCell getCell(String text, int alignment) {
        PdfPCell cell = new PdfPCell(new Phrase(text));
        cell.setPadding(0);
        cell.setHorizontalAlignment(alignment);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }

    private PdfPCell getTitleCell(String text) {
        Font bold = new Font(Font.FontFamily.UNDEFINED, 14, Font.BOLD);
        PdfPCell cell = new PdfPCell(new Phrase(text, bold));
        cell.setPadding(0);
        cell.setHorizontalAlignment(PdfPCell.ALIGN_CENTER);
        cell.setBorder(PdfPCell.NO_BORDER);
        return cell;
    }
}
