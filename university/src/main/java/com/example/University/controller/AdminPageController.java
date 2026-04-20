package com.example.University.controller;

import com.example.University.dto.WorkshopRequestDTO;
import com.example.University.dto.WorkshopResponseDTO;
import com.example.University.service.RegistrationService;
import com.example.University.service.WorkshopService;
import jakarta.validation.Valid;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/admin")
public class AdminPageController {

    private final WorkshopService workshopService;
    private final RegistrationService registrationService;

    public AdminPageController(WorkshopService workshopService, RegistrationService registrationService) {
        this.workshopService = workshopService;
        this.registrationService = registrationService;
    }

    @GetMapping("/workshops")
    public String listWorkshops(Model model) {
        model.addAttribute("workshops", workshopService.getAllWorkshops());
        return "admin/workshops";
    }

    @GetMapping("/workshops/new")
    public String newWorkshopForm(Model model) {
        model.addAttribute("workshopForm", new WorkshopRequestDTO());
        model.addAttribute("formAction", "/admin/workshops/new");
        model.addAttribute("pageTitle", "Create New Workshop");
        return "admin/workshop-form";
    }

    @PostMapping("/workshops/new")
    public String createWorkshop(@Valid @ModelAttribute("workshopForm") WorkshopRequestDTO dto,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("formAction", "/admin/workshops/new");
            model.addAttribute("pageTitle", "Create New Workshop");
            return "admin/workshop-form";
        }
        workshopService.createWorkshop(dto);
        redirectAttributes.addFlashAttribute("successMessage", "Workshop created successfully!");
        return "redirect:/admin/workshops";
    }

    @GetMapping("/workshops/{id}/edit")
    public String editWorkshopForm(@PathVariable Long id, Model model) {
        WorkshopResponseDTO w = workshopService.getWorkshopById(id);
        WorkshopRequestDTO form = new WorkshopRequestDTO();
        form.setTitle(w.getTitle());
        form.setDescription(w.getDescription());
        form.setLocation(w.getLocation());
        form.setStartDatetime(w.getStartDatetime());
        form.setTotalSeats(w.getTotalSeats());
        model.addAttribute("workshopForm", form);
        model.addAttribute("workshopId", id);
        model.addAttribute("formAction", "/admin/workshops/" + id + "/edit");
        model.addAttribute("pageTitle", "Edit Workshop");
        return "admin/workshop-form";
    }

    @PostMapping("/workshops/{id}/edit")
    public String updateWorkshop(@PathVariable Long id,
                                 @Valid @ModelAttribute("workshopForm") WorkshopRequestDTO dto,
                                 BindingResult bindingResult,
                                 RedirectAttributes redirectAttributes,
                                 Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("formAction", "/admin/workshops/" + id + "/edit");
            model.addAttribute("pageTitle", "Edit Workshop");
            model.addAttribute("workshopId", id);
            return "admin/workshop-form";
        }
        workshopService.updateWorkshop(id, dto);
        redirectAttributes.addFlashAttribute("successMessage", "Workshop updated successfully!");
        return "redirect:/admin/workshops";
    }

    @PostMapping("/workshops/{id}/cancel")
    public String cancelWorkshop(@PathVariable Long id, RedirectAttributes redirectAttributes) {
        workshopService.cancelWorkshop(id);
        redirectAttributes.addFlashAttribute("successMessage", "Workshop cancelled.");
        return "redirect:/admin/workshops";
    }

    @GetMapping("/workshops/{id}/registrations")
    public String viewRegistrations(@PathVariable Long id, Model model) {
        WorkshopResponseDTO workshop = workshopService.getWorkshopById(id);
        model.addAttribute("workshop", workshop);
        model.addAttribute("registrations", registrationService.getRegistrationsForWorkshop(id));
        return "admin/registrations";
    }
}
