package com.jee.spring_labs.tenant.controller;

import com.jee.spring_labs.owner.model.Apartment;
import com.jee.spring_labs.owner.model.Bill;
import com.jee.spring_labs.tenant.model.Consumption;
import com.jee.spring_labs.tenant.model.ConsumptionType;
import com.jee.spring_labs.tenant.service.TenantService;
import com.jee.spring_labs.user.service.NotificationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.time.LocalDate;
import java.util.*;

import static java.util.stream.Collectors.toList;

@Controller
@RequestMapping("/tenant")
public class TenantController {

    /* VIEW PATHS */
    private static final String TENANT_PAGE = "views/tenant/tenantPage";
    private static final String APARTMENT = "views/tenant/apartment";

    @Autowired
    private TenantService service;

    @Autowired
    private NotificationUtils notificationUtils;

    @Value("${microservices.pdfService.url}")
    private String pdfServiceUrl;

    @GetMapping
    public ModelAndView getTenantPage(@ModelAttribute("paySuccess") String paySuccess, @ModelAttribute("payError") String payError, HttpSession session) {

        ModelAndView mv = new ModelAndView(TENANT_PAGE, "role", "tenant");
        notificationUtils.addUserNotificationsToModelAndView(mv, session);

        if (StringUtils.hasText(paySuccess)) {
            mv.addObject("paySuccess", true);
        } else if (StringUtils.hasText(payError)) {
            mv.addObject("payError", true);
        }
        return mv;
    }

    /**
     * APARTMENTS
     */
    @GetMapping(path = {"/apartments", "/apartments/{id}"})
    public ModelAndView getApartments(@PathVariable(name = "id", required = false) Long id, HttpSession session,
                                      @ModelAttribute("consumptionsGeneratedSuccess") String consumptionsGeneratedSuccess) {
        ModelAndView mv;
        if (id != null) {
            mv = new ModelAndView(APARTMENT);
            Apartment apartment = service.getItemById(Apartment.class, id, (long) session.getAttribute("userId"));
            mv.addObject("apartment", apartment);
            mv.addObject("bills", apartment.getBills().stream().filter(bill -> !bill.isRemoved()).collect(toList()));
            mv.addObject("consumptions", apartment.getConsumptions().stream().filter(consumption -> !consumption.isRemoved()).collect(toList()));
        } else {
            mv = new ModelAndView(TENANT_PAGE, "apartments", service.getItems(Apartment.class, (long) session.getAttribute("userId")));
            mv.addObject("apartmentsQueried", true);
        }
        return mv;
    }

    @GetMapping("/leaveApartment/{id}")
    public ModelAndView leaveApartment(@PathVariable("id") long id, Locale locale, HttpSession session) {
        ModelAndView mv = new ModelAndView(TENANT_PAGE);
        if (service.leaveApartment(id, locale, (long) session.getAttribute("userId"))) {
            mv.addObject("apartmentLeaveSuccess", true);
        } else {
            mv.addObject("apartmentLeaveError", true);
        }
        return mv;
    }

    /**
     * BILLS
     */
    @GetMapping("/bills")
    public ModelAndView getBills(HttpSession session) {
        ModelAndView mv = new ModelAndView(TENANT_PAGE, "bills", service.getItems(Bill.class, (long) session.getAttribute("userId")));
        mv.addObject("billsQueried", true);
        mv.addObject("role", "tenant");
        mv.addObject("pdfServiceUrl", pdfServiceUrl + "/exportBill");
        return mv;
    }

    @GetMapping("/payBill/{id}")
    public ModelAndView getPayBillView(@PathVariable("id") long billId, HttpSession session, RedirectAttributes redirectAttributes) {
        Bill bill = service.getItemById(Bill.class, billId, (long) session.getAttribute("userId"));
        if (bill.isAccepted()) {
            return new ModelAndView(TENANT_PAGE, "role", "tenant");
        }
        bill.setDateOfPayment(LocalDate.now());
        boolean response = service.saveItem(Bill.class, bill, (long) session.getAttribute("userId"));
        if (response) {
            redirectAttributes.addFlashAttribute("paySuccess", true);
        } else {
            redirectAttributes.addFlashAttribute("payError", true);
        }
        return new ModelAndView("redirect:/tenant/", "role", "tenant");
    }

    /**
     * CONSUMPTION
     */
    @GetMapping("/consumptions/{billId}")
    public ModelAndView getBillView(@PathVariable("billId") long billId, HttpSession session) {
        ModelAndView mv = new ModelAndView("views/owner/bill", "bill", service.getItemById(Bill.class, billId, (long) session.getAttribute("userId")));
        mv.addObject("userType", "tenant");
        mv.addObject("pdfServiceUrl", pdfServiceUrl + "/exportBill");
        return mv;
    }

    @GetMapping("/getConsumptions/{apartmentId}")
    public ModelAndView getConsumptions(@PathVariable("apartmentId") long apartmentId, @RequestParam("type") String type, @RequestParam("month") String month, RedirectAttributes redirectAttributes, HttpSession session) {
        Apartment apartment = service.getItemById(Apartment.class, apartmentId, (long) session.getAttribute("userId"));
        if (apartment == null) {
            return new ModelAndView(TENANT_PAGE);
        }

        ModelAndView mv = new ModelAndView(APARTMENT);
        mv.addObject("apartment", apartment);
        mv.addObject("bills", apartment.getBills());
        mv.addObject("consumptions", service.filterConsumptions(apartmentId, type, month));

        return mv;
    }


/*    @Scheduled(cron = "2 * * * * ?")
    private void cronGenerateConsumptionsMock() {
        List<Apartment> apartments = service.cronGetAllApartments();
        for (Apartment apartment : apartments) {
            if (apartment.getTenant() == null) {
                continue;
            }
            List<Consumption> consumptions = addConsumption(apartment);
            for(Consumption consumption : consumptions) {
                service.saveItem(Consumption.class, consumption, apartment.getTenant().getId());
            }
            service.saveItem(Apartment.class, apartment, apartment.getTenant().getId());
        }
    }*/

    @Scheduled(cron = "0 55 23 28-31 * ?")
    private void cronGenerateConsumptions() {
        final Calendar day = Calendar.getInstance();
        if (day.get(Calendar.DATE) == day.getActualMaximum(Calendar.DATE)) {
            List<Apartment> apartments = service.cronGetAllApartments();
            for (Apartment apartment : apartments) {
                if (apartment.getTenant() == null) {
                    continue;
                }
                List<Consumption> consumptions = addConsumption(apartment);
                for (Consumption consumption : consumptions) {
                    service.saveItem(Consumption.class, consumption, apartment.getTenant().getId());
                }
                service.saveItem(Apartment.class, apartment, apartment.getTenant().getId());
            }
        }
    }

    private List<Consumption> addConsumption(Apartment apartment) {
        Consumption electricity = new Consumption();
        electricity.setType(ConsumptionType.ELECTRICITY);
        electricity.setApartment(apartment);

        //TODO: check
        Consumption gas = new Consumption();
        gas.setType(ConsumptionType.GAS);
        //gas.setApartment(apartment);
        apartment.addConsumption(gas);

        Consumption water = new Consumption();
        water.setType(ConsumptionType.WATER);
        //water.setApartment(apartment);
        apartment.addConsumption(water);

        Consumption waste = new Consumption();
        waste.setType(ConsumptionType.WASTE);
        // waste.setApartment(apartment);
        apartment.addConsumption(waste);

        Consumption heating = new Consumption();
        heating.setType(ConsumptionType.HEATING);
        //  heating.setApartment(apartment);
        apartment.addConsumption(heating);

        Consumption renovation = new Consumption();
        renovation.setType(ConsumptionType.RENOVATION);
        //  renovation.setApartment(apartment);
        apartment.addConsumption(renovation);

        List<Consumption> monthlyConsumptions = Arrays.asList(electricity, gas, water, waste, heating, renovation);

        for (Consumption consumption : monthlyConsumptions) {
            int dayDifference = Calendar.getInstance().getActualMaximum(Calendar.DAY_OF_MONTH);
            if (dayDifference > 0) {
                switch (consumption.getType()) {
                    case ELECTRICITY: {
                        double amount = /*consumption.getAmount() +*/ new Random().nextInt(15) * dayDifference;
                        consumption.setAmount(amount);
                        consumption.setCost(ConsumptionType.ELECTRICITY.getCostPerUnit() * consumption.getAmount());
                        break;
                    }
                    case GAS: {
                        double amount = /*consumption.getAmount() +*/ new Random().nextInt(5) * dayDifference;
                        consumption.setAmount(amount);
                        consumption.setCost(ConsumptionType.GAS.getCostPerUnit() * consumption.getAmount());
                        break;
                    }
                    case WATER: {
                        double amount = /*consumption.getAmount() +*/ (new Random().nextInt(1) + 1) * 0.1 * dayDifference;
                        consumption.setAmount(amount);
                        consumption.setCost(ConsumptionType.WATER.getCostPerUnit() * consumption.getAmount());
                        break;
                    }
                    case WASTE: {
                        double amount = /*consumption.getAmount() +*/ (new Random().nextInt(1) + 1) * 0.1 * dayDifference;
                        consumption.setAmount(amount);
                        consumption.setCost(ConsumptionType.WASTE.getCostPerUnit() * consumption.getAmount());
                        break;
                    }
                    case HEATING: {
                        double amount = /*consumption.getAmount() +*/ (new Random().nextInt(2) + 1) * 0.1 * dayDifference;
                        consumption.setAmount(amount);
                        consumption.setCost(ConsumptionType.HEATING.getCostPerUnit() * consumption.getAmount());
                        break;
                    }
                    case RENOVATION: {
                        double amount = /*consumption.getAmount() +*/ new Random().nextInt(1) * 0.1 * dayDifference;
                        consumption.setAmount(amount);
                        consumption.setCost(ConsumptionType.RENOVATION.getCostPerUnit() * consumption.getAmount());
                        break;
                    }
                }
                consumption.setDate(LocalDate.now());
            }
        }
        //TODO:check comment
        //apartment.getConsumptions().addAll(monthlyConsumptions);
        return monthlyConsumptions;
    }
}
