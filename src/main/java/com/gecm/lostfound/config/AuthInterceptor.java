package com.gecm.lostfound.config;

import com.gecm.lostfound.dto.UserSession;
import com.gecm.lostfound.util.SessionUtils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class AuthInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        UserSession user = SessionUtils.getCurrentUser();
        String uri = request.getRequestURI();

        if (uri.startsWith(request.getContextPath() + "/admin")) {
            if (user == null || !user.isAdmin()) {
                response.sendRedirect(request.getContextPath() + "/login?admin");
                return false;
            }
            return true;
        }

        if (uri.startsWith(request.getContextPath() + "/dashboard")) {
            if (user == null) {
                response.sendRedirect(request.getContextPath() + "/login");
                return false;
            }
        }

        return true;
    }
}
