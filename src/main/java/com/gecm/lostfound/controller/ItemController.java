package com.gecm.lostfound.controller;

import com.gecm.lostfound.dto.ItemView;
import com.gecm.lostfound.service.ClaimService;
import com.gecm.lostfound.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class ItemController {

    private final ItemService itemService;
    private final ClaimService claimService;

    public ItemController(ItemService itemService, ClaimService claimService) {
        this.itemService = itemService;
        this.claimService = claimService;
    }

    @GetMapping("/item/{id}")
    public String viewItem(@PathVariable Integer id, Model model) {
        return itemService.getItemWithUser(id)
                .map(item -> {
                    model.addAttribute("item", item);
                    return "item-view";
                })
                .orElse("redirect:/browse");
    }

    @PostMapping("/item/{id}/claim")
    public String submitClaim(@PathVariable Integer id,
                              @RequestParam String claimerName,
                              @RequestParam String claimerEmail,
                              @RequestParam(required = false) String message,
                              Model model) {
        ItemView item = itemService.getItemWithUser(id).orElse(null);
        if (item == null) {
            return "redirect:/browse";
        }

        try {
            claimService.submitClaim(id, claimerName, claimerEmail, message);
            model.addAttribute("claimMsg", "Claim request submitted. Admin will verify.");
        } catch (Exception ex) {
            model.addAttribute("claimMsg", "Unable to submit the claim right now. Please try again.");
        }

        model.addAttribute("item", item);
        return "item-view";
    }
}
