package com.jee.spring_labs.user.controller;

import com.jee.spring_labs.model.Request;
import com.jee.spring_labs.model.User;
import com.jee.spring_labs.user.dao.RequestRepository;
import com.jee.spring_labs.user.dao.UserRepository;
import com.jee.spring_labs.user.service.RequestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Locale;

@Controller
@RequestMapping("/userRequests")
public class RequestController {

    private static final String SEND_REQUEST = "/views/sendRequest";

    @Autowired
    private UserRepository userDao;

    @Autowired
    private RequestRepository requestDao;

    @Autowired
    private RequestService service;


    @GetMapping("/{role}/requests")
    public ModelAndView getUserRequests(@PathVariable("role") String role, HttpSession session,
                                        @RequestParam(name = "subject", required = false) String subjectParam, @RequestParam(name = "removed", required = false) Boolean removedParam) {
        if (role == null || (!role.equals("admin") && !role.equals("owner") && !role.equals("tenant"))) {
            return new ModelAndView("redirect:/home");
        }
        String viewName = role + "/" + role + "Page";
        String subject = subjectParam == null ? "" : subjectParam;
        boolean removed = role.equals("admin") && removedParam != null && removedParam;
        long userId = (long) session.getAttribute("userId");

        ModelAndView mv = new ModelAndView("/views/" + viewName);

        mv.addObject("requestsQueried", true);
        mv.addObject("requestsSent", service.getSentRequests(userId, subject, removed));
        mv.addObject("requestsReceived", service.getReceivedRequests(userId, subject, removed));
        return mv;
    }

    @GetMapping("/{role}/saveRequest")
    public ModelAndView getSaveRequestView(@PathVariable("role") String role, Authentication authentication, HttpServletRequest request) {
        if (role == null || (!role.equals("admin") && !role.equals("owner") && !role.equals("tenant"))) {
            return new ModelAndView("redirect:/home");
        }
        ModelAndView mv = new ModelAndView(SEND_REQUEST);
        HttpSession session = request.getSession();
        if (session.getAttribute("usernames") == null) {
            session.setAttribute("usernames", userDao.getUsernames(authentication.getName()));
        }

        mv.addObject("request", new Request());
        mv.addObject("usernames", session.getAttribute("usernames"));
        mv.addObject("userRole", role);
        return mv;
    }

    @PostMapping("/{role}/saveRequest")
    public String saveRequest(@PathVariable("role") String role, @ModelAttribute("request") Request request, Model model, BindingResult result,
                              Locale locale, Authentication authentication, HttpServletRequest httpServletRequest) {
        if (role == null || (!role.equals("admin") && !role.equals("owner") && !role.equals("tenant"))) {
            return "redirect:/home";
        }
        String viewName = "views/" + role + "/" + role + "Page";
        if (result.hasErrors()) {
            model.addAttribute("sendRequestError", true);
            return SEND_REQUEST;
        }
        User sender = (User) authentication.getPrincipal();
        String[] recipientNames = httpServletRequest.getParameterValues("recipientNames[]");

        request.setRecipients(new HashSet<>(userDao.getUsersByUsernames(Arrays.asList(recipientNames))));
        request.setSender(sender);
        request.setSendingDate(LocalDateTime.now().withNano(0));
        requestDao.save(request);

        model.addAttribute("sendRequestSuccess", true);
        model.addAttribute("returnUrl", viewName);
        return "redirect:/userRequests/" + role + "/saveRequest";
    }
}
