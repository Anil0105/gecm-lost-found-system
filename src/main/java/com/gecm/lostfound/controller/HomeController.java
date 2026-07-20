package com.gecm.lostfound.controller;

import com.gecm.lostfound.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final ItemService itemService;

    public HomeController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping({"/", "/index"})
    public String index(Model model) {
        model.addAttribute("recentItems", itemService.getApprovedRecent(6));
        return "index";
    }
}
