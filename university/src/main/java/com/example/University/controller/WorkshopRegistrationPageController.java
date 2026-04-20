package com.example.University.controller;

import com.example.University.service.RegistrationService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
public class WorkshopRegistrationPageController {

    private final RegistrationService registrationService;

    public WorkshopRegistrationPageController(RegistrationService registrationService) {
        this.registrationService = registrationService;
    }

    @PostMapping("/workshops/{id}/register")
    public String registerForWorkshop(@PathVariable Long id,
                                      Authentication authentication,
                                      RedirectAttributes redirectAttributes) {
        try {
            registrationService.registerUserForWorkshop(id, authentication.getName());
            redirectAttributes.addFlashAttribute("successMessage", "Successfully registered for workshop!");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/workshops/" + id;
    }

    @PostMapping("/registrations/{registrationId}/cancel")
    public String cancelRegistration(@PathVariable Long registrationId,
                                     Authentication authentication,
                                     RedirectAttributes redirectAttributes) {
        try {
            boolean isAdmin = authentication.getAuthorities().stream()
                    .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
            registrationService.cancelRegistration(registrationId, authentication.getName(), isAdmin);
            redirectAttributes.addFlashAttribute("successMessage", "Registration cancelled successfully.");
        } catch (Exception e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/my/registrations";
    }
}
