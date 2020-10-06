package com.jee.spring_labs.user.controller;

import com.jee.spring_labs.owner.dao.BillRepository;
import com.jee.spring_labs.owner.model.Bill;
import com.jee.spring_labs.owner.model.BillDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.*;
import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@RestController
@RequestMapping("/exportBill")
public class BillPdfController {

    @Value("${microservices.pdfService.url}")
    private String pdfServiceUrl;

    @Autowired
    private BillRepository repository;

    @Autowired
    private RestTemplate restTemplate;

    @GetMapping("/{billId}")
    @ResponseBody
    public ResponseEntity<byte[]> exportToPdf(@PathVariable("billId") long billId, HttpServletRequest request) throws Exception {
        Bill bill = repository.findById(billId).get();
        BillDto dto = BillDto.fromBill(bill);

        HttpSession session = request.getSession();
        OAuth2AccessToken auth2AccessToken = (OAuth2AccessToken) session.getAttribute("accessToken");
        String authorizationToken = auth2AccessToken.getTokenType() + " " + auth2AccessToken.getValue();

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", authorizationToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<BillDto> entity = new HttpEntity<>(dto, headers);
        ResponseEntity<byte[]> responseBytes = restTemplate.postForEntity(pdfServiceUrl + "/exportBill", entity, byte[].class);

        byte[] contents = responseBytes.getBody();

        HttpHeaders responseHeaders = new HttpHeaders();
        responseHeaders.setContentType(MediaType.APPLICATION_PDF);
        String filename = "bill_" + billId + ".pdf";
        responseHeaders.setContentDispositionFormData(filename, filename);
        responseHeaders.setCacheControl("must-revalidate, post-check=0, pre-check=0");

        return new ResponseEntity<>(contents, responseHeaders, HttpStatus.OK);
    }
}
