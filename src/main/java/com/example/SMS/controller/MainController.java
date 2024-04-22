package com.example.SMS.controller;

import com.example.SMS.dto.*;
import com.example.SMS.entity.*;
import com.example.SMS.service.*;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
public class MainController {
    @Autowired
    private UserService userService;

    @Autowired
    private AccommodationService accommodationService;
    @Autowired
    private MarketPlaceService marketPlaceService;
    @Autowired
    private RideService rideService;
    @Autowired
    private EventService eventService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        UserDto user = new UserDto();
        model.addAttribute("user", user);
        return "register";
    }

    @PostMapping("/register/save")
    public String registration(@Valid @ModelAttribute("user") UserDto userDto, BindingResult result, Model model) {
        User existingUser = userService.findUserByEmail(userDto.getEmail());

        if (existingUser != null && existingUser.getEmail() != null && !existingUser.getEmail().isEmpty()) {
            result.rejectValue("email", null, "There is already an account registered with the same email");
        }

        if (result.hasErrors()) {
            model.addAttribute("user", userDto);
            return "/register";
        }

        userService.saveUser(userDto);
        return "redirect:/register?success";
    }

    @GetMapping("/register/forgot-password")
    public String getForgotPassword(Model model) {
        return "forgot-password";
    }

    @PostMapping("/register/forgot-password-email")
    @ResponseBody
    public boolean forgotPasswordEmail(@RequestBody SendForgotPasswordEmail request) {
        return userService.sendForgotPasswordEmail(request.getEmailAddress());
    }

    @PostMapping("/register/change-password")
    @ResponseBody
    public String changePassword(@RequestBody ChangePasswordForm changePasswordForm) {
        userService.changePassword(changePasswordForm);
        return "redirect:/login";
    }


    @GetMapping("/index")
    public String home() {
        return "index";
    }

    @GetMapping("/events")
    public String events(Model model) {
        List<Event> events = eventService.getAvailableEvents();
        model.addAttribute("events", events);
        model.addAttribute("success", "");
        model.addAttribute("message", "");

        return "event";
    }

    @GetMapping("/certification-assistance")
    public String certification(Model model) {
        model.addAttribute("userCertificates", userService.getUserRequests());
        
        if(!model.containsAttribute("success")){
        	model.addAttribute("success", "");
        }

        if(!model.containsAttribute("message")){
        	model.addAttribute("message", "");
        }

        return "certification-assistance";
    }

    @PostMapping("/add-request")
    public String addRequest(@ModelAttribute("CertificateAssistanceForm") CertificateAssistanceForm certificateAssistanceForm,
                             @RequestParam("file") MultipartFile file,
                             RedirectAttributes redirectAttributes) {
        try {
            userService.addRequest(certificateAssistanceForm, file);
            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("message", "Request added/updated successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("message", "Given email not associated with us.");
        }

        return "redirect:/certification-assistance";
    }
    
    @GetMapping("/delete-request")
    public String deleteRequest(@RequestParam(name = "requestId") Long requestId, Model model, RedirectAttributes redirectAttributes) {
        try {
            userService.deleteRequest(requestId);
            redirectAttributes.addFlashAttribute("success", true);
            redirectAttributes.addFlashAttribute("message", "Request deleted successfully");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("success", false);
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }

        return "redirect:/certification-assistance";
    }

    @GetMapping("/accommodation")
    public String housing(Model model) {
        List<Accommodation> accommodations = accommodationService.getAvailableAccomodations();
        model.addAttribute("accommodations", accommodations);
        model.addAttribute("success", "");
        model.addAttribute("message", "");

        return "accommodation";
    }

    @GetMapping("/rides")
    public String rides(Model model) {
        List<Ride> rides = rideService.getAvailableRides();
        model.addAttribute("rides", rides);
        model.addAttribute("success", "");
        model.addAttribute("message", "");

        return "rides";
    }

    @GetMapping("/student-marketplace")
    public String studentMarketplace(Model model) {
        List<MarketPlace> marketPlaces = marketPlaceService.getAvailableMarketPlaces();
        model.addAttribute("marketPlaces", marketPlaces);
        model.addAttribute("success", "");
        model.addAttribute("message", "");

        return "market-place";
    }
}