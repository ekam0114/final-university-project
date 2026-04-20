package com.example.University.controller;

import com.example.University.dto.WorkshopResponseDTO;
import com.example.University.service.RegistrationService;
import com.example.University.service.WorkshopService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class PageController {

    private final WorkshopService workshopService;
    private final RegistrationService registrationService;

    public PageController(WorkshopService workshopService, RegistrationService registrationService) {
        this.workshopService = workshopService;
        this.registrationService = registrationService;
    }

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("workshops", workshopService.getAllActiveUpcomingWorkshops());
        return "home";
    }

    @GetMapping("/workshops/{id}")
    public String workshopDetail(@PathVariable Long id, Model model, Authentication authentication) {
        WorkshopResponseDTO workshop = workshopService.getWorkshopById(id);
        model.addAttribute("workshop", workshop);

        if (authentication != null && authentication.isAuthenticated()) {
            String email = authentication.getName();
            List<?> myRegs = registrationService.getMyRegistrations(email);
            boolean alreadyRegistered = myRegs.stream().anyMatch(r -> {
                if (r instanceof com.example.University.dto.RegistrationResponseDTO reg) {
                    return reg.getWorkshopId().equals(id) && "ACTIVE".equals(reg.getStatus());
                }
                return false;
            });
            model.addAttribute("alreadyRegistered", alreadyRegistered);
            Long registrationId = myRegs.stream()
                .filter(r -> r instanceof com.example.University.dto.RegistrationResponseDTO reg
                    && reg.getWorkshopId().equals(id) && "ACTIVE".equals(reg.getStatus()))
                .map(r -> ((com.example.University.dto.RegistrationResponseDTO) r).getId())
                .findFirst().orElse(null);
            model.addAttribute("registrationId", registrationId);
        }
        return "workshop-detail";
    }

    @GetMapping("/my/registrations")
    public String myRegistrations(Authentication authentication, Model model) {
        String email = authentication.getName();
        model.addAttribute("registrations", registrationService.getMyRegistrations(email));
        return "my-registrations";
    }
}
