package com.gecm.lostfound.controller;

import com.gecm.lostfound.model.ClaimStatus;
import com.gecm.lostfound.model.ItemStatus;
import com.gecm.lostfound.model.UserRole;
import com.gecm.lostfound.repository.UserRepository;
import com.gecm.lostfound.service.ClaimService;
import com.gecm.lostfound.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/admin")
public class AdminController {

    private final ItemService itemService;
    private final ClaimService claimService;
    private final UserRepository userRepository;

    public AdminController(ItemService itemService,
                           ClaimService claimService,
                           UserRepository userRepository) {
        this.itemService = itemService;
        this.claimService = claimService;
        this.userRepository = userRepository;
    }

    @GetMapping
    public String admin(@RequestParam(value = "view", defaultValue = "dashboard") String view,
                        @RequestParam(value = "action", required = false) String action,
                        @RequestParam(value = "id", required = false) Integer id,
                        Model model) {
        if (action != null && id != null) {
            handleAction(action, id);
            String redirectView = ("verify_claim".equals(action) || "reject_claim".equals(action))
                    ? "claims" : "dashboard";
            return "redirect:/admin?view=" + redirectView;
        }

        if ("claims".equals(view)) {
            model.addAttribute("claims", claimService.getAllClaimsWithItemTitle());
            return "admin-claims";
        }

        model.addAttribute("totalUsers", userRepository.countByRole(UserRole.user));
        model.addAttribute("totalItems", itemService.countAll());
        model.addAttribute("pendingItems", itemService.countByStatus(ItemStatus.pending));
        model.addAttribute("approvedItems", itemService.countByStatus(ItemStatus.approved));
        model.addAttribute("requestedClaims", claimService.countRequested());
        model.addAttribute("users", userRepository.findByRoleOrderByCreatedAtDesc(UserRole.user));
        model.addAttribute("items", itemService.getAllItemsWithUser());
        return "admin-dashboard";
    }

    private void handleAction(String action, Integer id) {
        switch (action) {
            case "approve" -> itemService.updateStatus(id, ItemStatus.approved);
            case "returned" -> itemService.updateStatus(id, ItemStatus.returned);
            case "delete" -> itemService.updateStatus(id, ItemStatus.deleted);
            case "verify_claim" -> claimService.updateStatus(id, ClaimStatus.verified);
            case "reject_claim" -> claimService.updateStatus(id, ClaimStatus.rejected);
            default -> {
            }
        }
    }
}
