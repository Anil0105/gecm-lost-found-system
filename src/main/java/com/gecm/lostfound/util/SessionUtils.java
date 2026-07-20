package com.gecm.lostfound.util;

import com.gecm.lostfound.dto.UserSession;
import com.gecm.lostfound.model.UserRole;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

public final class SessionUtils {

    public static final String USER_SESSION_KEY = "user";

    private SessionUtils() {
    }

    public static HttpSession currentSession() {
        ServletRequestAttributes attributes =
                (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        if (attributes == null) {
            return null;
        }
        return attributes.getRequest().getSession(true);
    }

    public static UserSession getCurrentUser() {
        HttpSession session = currentSession();
        if (session == null) {
            return null;
        }
        Object value = session.getAttribute(USER_SESSION_KEY);
        if (value instanceof UserSession userSession) {
            return userSession;
        }
        return null;
    }

    public static void setCurrentUser(UserSession user) {
        HttpSession session = currentSession();
        if (session != null) {
            session.setAttribute(USER_SESSION_KEY, user);
        }
    }

    public static void clearCurrentUser() {
        HttpSession session = currentSession();
        if (session != null) {
            session.removeAttribute(USER_SESSION_KEY);
        }
    }

    public static boolean isAdmin() {
        UserSession user = getCurrentUser();
        return user != null && user.getRole() == UserRole.admin;
    }

    public static boolean isLoggedIn() {
        return getCurrentUser() != null;
    }
}
