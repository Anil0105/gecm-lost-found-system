package com.gecm.lostfound.controller;

import com.gecm.lostfound.service.ItemService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class
PublicController {

    private final ItemService itemService;

    public PublicController(ItemService itemService) {
        this.itemService = itemService;
    }

    @GetMapping({"/browse", "/public"})
    public String browse(@RequestParam(value = "type", defaultValue = "all") String type,
                         @RequestParam(value = "q", defaultValue = "") String q,
                         Model model) {
        model.addAttribute("type", type);
        model.addAttribute("q", q);
        model.addAttribute("items", itemService.searchApproved(type, q));
        return "browse";
    }
}
