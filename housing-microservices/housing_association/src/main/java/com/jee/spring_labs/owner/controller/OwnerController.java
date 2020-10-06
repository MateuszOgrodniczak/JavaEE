package com.jee.spring_labs.owner.controller;

import com.jee.spring_labs.model.Building;
import com.jee.spring_labs.model.User;
import com.jee.spring_labs.model.UserRole;
import com.jee.spring_labs.owner.model.Apartment;
import com.jee.spring_labs.owner.model.Bill;
import com.jee.spring_labs.owner.service.OwnerService;
import com.jee.spring_labs.owner.validator.OwnerBuildingValidator;
import com.jee.spring_labs.tenant.model.Consumption;
import com.jee.spring_labs.user.service.NotificationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.*;

@Controller
@RequestMapping("/owner")
public class OwnerController {

    private static final String OWNER_PAGE = "views/owner/ownerPage";
    private static final String BUILDING = "views/owner/building";
    private static final String BILL_INFO = "views/owner/bill";
    private static final String SAVE_APARTMENT = "views/owner/saveApartment";

    @Value("${microservices.pdfService.url}")
    private String pdfServiceUrl;

    @Autowired
    private OwnerService service;

    @Autowired
    private OwnerBuildingValidator buildingValidator;

    @Autowired
    private NotificationUtils notificationUtils;

    @GetMapping
    public ModelAndView getOwnerPage(@ModelAttribute("addedToBillSuccess") String addedSuccess, @ModelAttribute("addedToBillError") String addedError,
                                     @ModelAttribute("acceptBillSuccess") String acceptSuccess, @ModelAttribute("acceptBillError") String acceptError,
                                     @ModelAttribute("removeSuccess") String removeSuccess, @ModelAttribute("removeError") String removeError, HttpSession session) {
        ModelAndView mv = new ModelAndView(OWNER_PAGE, "role", "owner");
        notificationUtils.addUserNotificationsToModelAndView(mv, session);

        if (StringUtils.hasText(addedSuccess)) {
            mv.addObject("addedToBillSuccess", true);
        } else if (StringUtils.hasText(addedError)) {
            mv.addObject("addedToBillError", true);
        }
        if (StringUtils.hasText(acceptSuccess)) {
            mv.addObject("acceptBillSuccess", true);
        } else if (StringUtils.hasText(acceptError)) {
            mv.addObject("acceptBillError", true);
        }
        if (StringUtils.hasText(removeSuccess)) {
            mv.addObject("removeSuccess", true);
        } else if (StringUtils.hasText(removeError)) {
            mv.addObject("removeError", true);
        }
        return mv;
    }

    /**
     * BUILDINGS
     */
    @GetMapping("/buildings")
    public ModelAndView getBuildings(@RequestParam("name") String name, HttpSession session) {
        ModelAndView mv = new ModelAndView(OWNER_PAGE, "buildings", service.getItems(Building.class, name, (long) session.getAttribute("userId")));
        mv.addObject("buildingsQueried", true);
        return mv;
    }

    @GetMapping("/saveBuilding/{id}")
    public ModelAndView getSaveBuildingView(@PathVariable(name = "id", required = false) Long id, @ModelAttribute("saveSuccess") String saveSuccess, HttpSession session) {
        Building building = service.getItemById(Building.class, id, (long) session.getAttribute("userId"));
        ModelAndView mv = new ModelAndView(BUILDING, "building", building);

        Set<User> tenants = new HashSet<>();
        building.getApartments().forEach(apartment -> tenants.add(apartment.getTenant()));
        mv.addObject("tenants", tenants);

        if (saveSuccess != null) {
            mv.addObject("saveSuccess", saveSuccess);
        }
        return mv;
    }

    @PostMapping("/saveBuilding")
    public String saveBuilding(@ModelAttribute("building") Building building, BindingResult result, Model model, HttpSession session, RedirectAttributes redirectAttributes) {
        buildingValidator.validate(building, result);
        if (result.hasErrors()) {
            model.addAttribute("saveError", true);
            return BUILDING;
        }

        if (!service.saveItem(Building.class, building, (long) session.getAttribute("userId"))) {
            model.addAttribute("saveError", true);
            return BUILDING;
        } else {
            redirectAttributes.addAttribute("saveSuccess", true);
        }
        return "redirect:/owner/saveBuilding/" + building.getId();
    }

    @GetMapping("/removeBuilding/{id}")
    public ModelAndView removeBuilding(@PathVariable("id") long id, HttpSession session, RedirectAttributes redirectAttributes) {
        ModelAndView mv = new ModelAndView("redirect:/owner");
        boolean response = service.removeItem(Building.class, id, (long) session.getAttribute("userId"));
        if (response) {
            redirectAttributes.addFlashAttribute("removeSuccess", true);
        } else {
            redirectAttributes.addFlashAttribute("removeError", true);
        }
        return mv;
    }

    /**
     * BILLS
     */
    @GetMapping("/bills")
    public ModelAndView getBills(HttpSession session) {
        ModelAndView mv = new ModelAndView(OWNER_PAGE, "bills", service.getItems(Bill.class, null, (long) session.getAttribute("userId")));
        mv.addObject("billsQueried", true);
        mv.addObject("pdfServiceUrl", pdfServiceUrl);
        return mv;
    }

    @GetMapping("/billInfo/{id}")
    public ModelAndView getBillView(@PathVariable(name = "id") long id, HttpSession session) {
        ModelAndView mv = new ModelAndView(BILL_INFO, "bill", service.getItemById(Bill.class, id, (long) session.getAttribute("userId")));
        mv.addObject("userType", "owner");
        mv.addObject("pdfServiceUrl", pdfServiceUrl);
        return mv;
    }

    @GetMapping("/acceptBill/{id}")
    public String saveBill(@PathVariable("id") long id, HttpSession session, RedirectAttributes redirectAttributes) {
        Bill bill = service.getItemById(Bill.class, id, (long) session.getAttribute("userId"));
        if (bill == null || bill.isAccepted()) {
            return "redirect:/owner";
        }
        bill.setAccepted(true);
        boolean response = service.saveItem(Bill.class, bill, (long) session.getAttribute("userId"));
        if (response) {
            redirectAttributes.addFlashAttribute("acceptBillSuccess", true);
        } else redirectAttributes.addFlashAttribute("acceptBillError", true);
        return "redirect:/owner";
    }

    @GetMapping("/removeBill/{id}")
    public ModelAndView removeBill(@PathVariable("id") long id, HttpSession session, RedirectAttributes redirectAttributes) {
        ModelAndView mv = new ModelAndView("redirect:/owner");
        boolean response = service.removeItem(Bill.class, id, (long) session.getAttribute("userId"));
        if (response) {
            redirectAttributes.addFlashAttribute("removeSuccess", true);
        } else {
            redirectAttributes.addFlashAttribute("removeError", true);
        }
        return mv;
    }

    /**
     * CONSUMPTIONS
     */
    @GetMapping("/consumptions")
    public ModelAndView getConsumptions(HttpSession session) {
        ModelAndView mv = new ModelAndView(OWNER_PAGE, "consumptions", service.getItems(Consumption.class, null, (long) session.getAttribute("userId")));
        mv.addObject("consumptionsQueried", true);
        return mv;
    }

    @GetMapping("/addToBill/{id}")
    public ModelAndView addConsumptionToBill(@PathVariable("id") long id, HttpSession session) {
        ModelAndView mv = new ModelAndView(OWNER_PAGE);
        if (service.addToBill(id, (long) session.getAttribute("userId"))) {
            mv.addObject("addedToBillSuccess", true);
        } else {
            mv.addObject("addedToBillError", false);
        }
        return mv;
    }

    /**
     * APARTMENTS
     */
    @GetMapping(path = {"/saveApartment/{buildingId}", "/saveApartment/{buildingId}/{id}"})
    public ModelAndView getSaveApartmentView(@PathVariable("buildingId") long buildingId, @PathVariable(name = "id", required = false) Long id, HttpSession session,
                                             @ModelAttribute("saveSuccess") String saveSuccess) {
        if (id == null) {
            Apartment apartment = new Apartment();
            apartment.setBuilding(service.getItemById(Building.class, buildingId, (long) session.getAttribute("userId")));
            return new ModelAndView(SAVE_APARTMENT, "apartment", apartment);
        }
        Apartment apartment = service.getItemById(Apartment.class, id, (long) session.getAttribute("userId"));
        if (apartment.getBuilding().getId() != buildingId) {
            return new ModelAndView(OWNER_PAGE);
        }
        ModelAndView mv = new ModelAndView(SAVE_APARTMENT, "apartment", apartment);
        if (saveSuccess != null) mv.addObject("saveSuccess", saveSuccess);
        return mv;
    }

    @PostMapping("/saveApartment")
    public String saveApartment(@ModelAttribute("apartment") Apartment apartment, Model model, BindingResult result, HttpSession session,
                                RedirectAttributes redirectAttributes) {
        if (apartment.getRoomNumber() <= 0) {
            result.rejectValue("roomNumber", "error.apartment.room_number.invalid");
        }
        if (service.isRoomNumberTaken(apartment.getRoomNumber(), apartment.getBuilding().getId(), apartment.getId())) {
            result.rejectValue("roomNumber", "error.apartment.room_number.taken");
        }
        User tenant = service.getUserByUsername(apartment.getTenant().getUsername());
        if (!(tenant != null && tenant.getRole().equals(UserRole.TENANT) /*&& (tenant.getApartment() == null || tenant.getApartment().getId() == apartment.getId())*/)) {
            result.rejectValue("tenant.username", "error.apartment.tenant");
        }
        if (result.hasErrors()) {
            model.addAttribute("saveError", true);
            model.addAttribute("apartment", apartment);
            return SAVE_APARTMENT;
        }
        apartment.setTenant(tenant);

        boolean response = service.saveItem(Apartment.class, apartment, (long) session.getAttribute("userId"));
        if (!response) {
            return OWNER_PAGE;
        } else {
            redirectAttributes.addFlashAttribute("saveSuccess", true);
        }
        return "redirect:/owner/saveApartment/" + apartment.getBuilding().getId() + "/" + apartment.getId();
    }

    @GetMapping("/removeApartment/{id}")
    public ModelAndView removeApartment(@PathVariable("id") long id, HttpSession session, RedirectAttributes redirectAttributes) {
        ModelAndView mv = new ModelAndView("redirect:/owner/");
        boolean response = service.removeItem(Apartment.class, id, (long) session.getAttribute("userId"));
        if (response) {
            redirectAttributes.addFlashAttribute("removeSuccess", true);
        } else {
            redirectAttributes.addFlashAttribute("removeError", true);
        }
        return mv;
    }
}
