package com.gecm.lostfound.config;

import com.gecm.lostfound.dto.UserSession;
import com.gecm.lostfound.util.SessionUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

@ControllerAdvice
public class GlobalModelAdvice {

    @ModelAttribute("currentUser")
    public UserSession currentUser() {
        return SessionUtils.getCurrentUser();
    }
}
