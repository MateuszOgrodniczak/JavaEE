package com.jee.spring_labs.admin.controller;

import com.jee.spring_labs.admin.service.AdminService;
import com.jee.spring_labs.admin.validator.AdminBuildingValidator;
import com.jee.spring_labs.model.*;
import com.jee.spring_labs.user.dao.NotificationRepository;
import com.jee.spring_labs.user.model.Notification;
import com.jee.spring_labs.user.service.NotificationUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpSession;
import java.util.List;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private static final String ADMIN_PAGE = "views/admin/adminPage";
    private static final String SAVE_USER = "views/admin/saveUser";
    private static final String SAVE_BUILDING = "views/admin/saveBuilding";
    private static final String SAVE_BUILDING_REDIRECT = "redirect:/admin/saveBuilding/";
    private static final String XML_SUFFIX = ".xml";
    private static final String JSON_SUFFIX = ".json";

    @Autowired
    private AdminService service;

    @Autowired
    private AdminBuildingValidator buildingValidator;

    @Autowired
    private NotificationUtils notificationUtils;

    @GetMapping
    public ModelAndView getAdminPage(@ModelAttribute("removeSuccess") String removeSuccess,
                                     @ModelAttribute("removeError") String removeError, HttpSession session) {

        ModelAndView mv = new ModelAndView(ADMIN_PAGE, "role", "admin");
        notificationUtils.addUserNotificationsToModelAndView(mv, session);

        if (StringUtils.hasText(removeSuccess)) {
            mv.addObject("removeSuccess", true);
        } else if (StringUtils.hasText(removeError)) {
            mv.addObject("removeError", true);
        }
        return mv;
    }

    /**
     * ADMIN PAGE - COLLECTION GETTERS
     */
    @GetMapping("/users")
    public ModelAndView getUsers(@RequestParam(name = "username", required = false) String username,
                                 @RequestParam(name = "suffix", required = false) String suffix,
                                 @RequestParam(name = "removed", required = false) Boolean removed) {
        ModelAndView mv = redirectToRestController("users", suffix, username);
        if (mv == null) {
            mv = new ModelAndView(ADMIN_PAGE, "users", service.getItems(User.class, username, removed));
            mv.addObject("usersQueried", true);
        }
        return mv;
    }

    @GetMapping("/buildings")
    public ModelAndView getBuildings(@RequestParam(name = "name", required = false) String name, @RequestParam(name = "suffix", required = false) String suffix,
                                     @RequestParam(name = "removed", required = false) Boolean removed) {
        ModelAndView mv = redirectToRestController("buildings", suffix, name);
        if (mv == null) {
            mv = new ModelAndView(ADMIN_PAGE, "buildings", service.getItems(Building.class, name, removed));
            mv.addObject("buildingsQueried", true);
        }
        return mv;
    }

    @GetMapping("/applications")
    public ModelAndView getRequests() {
        return new ModelAndView("redirect:/userApplications/admin/applications");
    }

    private ModelAndView redirectToRestController(String url, String suffix, String filter) {
        if (!StringUtils.isEmpty(suffix)) {
            if (filter == null) {
                filter = "";
            }
            if (suffix.equals(XML_SUFFIX)) {
                return new ModelAndView("redirect:/" + url + "/" + filter + XML_SUFFIX);
            }
            if (suffix.equals(JSON_SUFFIX)) {
                return new ModelAndView("redirect:/" + url + "/" + filter);
            }
        }
        return null;
    }

    /**
     * BUILDINGS
     */
    @GetMapping(path = {"/saveBuilding/{id}", "/saveBuilding"})
    public ModelAndView getSaveBuildingView(@PathVariable(name = "id", required = false) Long id, @ModelAttribute("saveSuccess") String saveSuccess) {
        Building building;
        if (id == null) {
            building = new Building();
            building.setAddress(new Address());
        } else {
            building = service.getItemById(Building.class, id);
        }
        ModelAndView mv = new ModelAndView(SAVE_BUILDING);
        mv.addObject("building", building);
        if (!StringUtils.isEmpty(saveSuccess)) {
            mv.addObject("saveSuccess", true);
        }
        return mv;
    }

    @PostMapping("/saveBuilding")
    public String saveBuilding(@ModelAttribute("building") Building building, BindingResult result, Model model, RedirectAttributes redirectAttributes) {
        buildingValidator.validate(building, result);
        if (result.hasErrors()) {
            model.addAttribute("saveError", true);
            return SAVE_BUILDING;
        }
        User owner = service.getUserByUserName(building.getOwner().getUsername());
        if (owner != null &&
                owner.getRole().equals(UserRole.OWNER) &&
                (owner.getBuilding() == null || owner.getBuilding().getId() == building.getId())) {
            owner.setBuilding(building);
            if (!service.saveItem(building)) {
                result.rejectValue("name", "error.constraint.unique");
                model.addAttribute("saveSuccess", false);
                return SAVE_BUILDING;
            }
        } else {
            result.rejectValue("owner.username", "error.building.owner");
            model.addAttribute("saveSuccess", false);
            return SAVE_BUILDING;
        }
        redirectAttributes.addFlashAttribute("saveSuccess", true);
        return SAVE_BUILDING_REDIRECT + building.getId();
    }

    @GetMapping("/removeBuilding/{id}")
    public ModelAndView removeBuilding(@PathVariable("id") long id, RedirectAttributes redirectAttributes) {
        ModelAndView mv = new ModelAndView("redirect:/admin");
        boolean response = service.removeItem(Building.class, id);

        if (response) {
            redirectAttributes.addFlashAttribute("removeSuccess", true);
        } else {
            redirectAttributes.addFlashAttribute("removeError", true);
        }
        return mv;
    }

    @GetMapping("/activateBuilding/{id}")
    public ModelAndView activateBuilding(@PathVariable long id, RedirectAttributes redirectAttributes) {
        ModelAndView mv = new ModelAndView("redirect:/admin");
        boolean response = service.activateItem(Building.class, id);

        if (response) {
            redirectAttributes.addFlashAttribute("saveSuccess", true);
        } else {
            redirectAttributes.addFlashAttribute("saveError", true);
        }
        return mv;
    }

    /**
     * USERS
     */
    @GetMapping("/saveUser/{id}")
    public ModelAndView getSaveUserView(@PathVariable("id") long id, @ModelAttribute("saveSuccess") String saveSuccess) {
        User user = service.getItemById(User.class, id);
        UserDto userDto = new UserDto();
        userDto.setId(user.getId());
        userDto.setEnabled(user.isEnabled());
        userDto.setRemoved(user.isRemoved());
        userDto.setUsername(user.getUsername());
        ModelAndView mv = new ModelAndView(SAVE_USER, "user", userDto);
        if (saveSuccess != null) mv.addObject("saveSuccess", saveSuccess);
        return mv;
    }

    @PostMapping("/saveUser")
    public String saveUser(@ModelAttribute("user") UserDto userDto, Model model, BindingResult result, RedirectAttributes redirectAttributes) {
        if (result.hasErrors()) {
            model.addAttribute("saveError", true);
            return SAVE_USER;
        }
        User user = service.getItemById(User.class, userDto.getId());
        user.setRemoved(userDto.isRemoved());
        user.setEnabled(userDto.isEnabled());

        if (!service.saveItem(user)) {
            model.addAttribute("saveError", true);
            return SAVE_USER;
        }
        redirectAttributes.addFlashAttribute("saveSuccess", true);
        return "redirect:/admin/saveUser/" + user.getId();
    }

    @GetMapping("/removeUser/{id}")
    public ModelAndView removeUser(@PathVariable("id") long id, RedirectAttributes redirectAttributes) {
        ModelAndView mv = new ModelAndView("redirect:/admin");
        boolean response = service.removeItem(User.class, id);

        if (response) {
            redirectAttributes.addFlashAttribute("removeSuccess", true);
        } else {
            redirectAttributes.addFlashAttribute("removeError", true);
        }
        return mv;
    }

    @GetMapping("/activateUser/{id}")
    public ModelAndView activateUser(@PathVariable long id, RedirectAttributes redirectAttributes) {
        ModelAndView mv = new ModelAndView("redirect:/admin");
        boolean response = service.activateItem(User.class, id);

        if (response) {
            redirectAttributes.addFlashAttribute("saveSuccess", true);
        } else {
            redirectAttributes.addFlashAttribute("saveError", true);
        }
        return mv;
    }

    //  @Scheduled(cron = "2 * * * * ?")
}
