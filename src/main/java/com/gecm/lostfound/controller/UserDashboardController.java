package com.gecm.lostfound.controller;

import com.gecm.lostfound.dto.UserSession;
import com.gecm.lostfound.model.ItemType;
import com.gecm.lostfound.service.ItemService;
import com.gecm.lostfound.util.SessionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Controller
@RequestMapping("/dashboard")
public class UserDashboardController {

    private final ItemService itemService;

    public UserDashboardController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping
    public String dashboard(Model model) {
        UserSession user = SessionUtils.getCurrentUser();
        model.addAttribute("user", user);
        model.addAttribute("myItems", itemService.getUserItems(user.getId()));
        return "user-dashboard";
    }

    @PostMapping("/add-item")
    public String addItem(@RequestParam ItemType type,
                          @RequestParam String title,
                          @RequestParam String description,
                          @RequestParam String location,
                          @RequestParam(value = "image", required = false) MultipartFile image,
                          Model model) throws IOException {
        UserSession user = SessionUtils.getCurrentUser();
        String error = itemService.addItem(user.getId(), type, title, description, location, image);
        if (error != null) {
            model.addAttribute("msg", error);
        } else {
            model.addAttribute("msg", "Item submitted and awaiting admin approval.");
        }
        model.addAttribute("user", user);
        model.addAttribute("myItems", itemService.getUserItems(user.getId()));
        return "user-dashboard";
    }
}
