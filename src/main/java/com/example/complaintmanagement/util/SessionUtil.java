package com.example.complaintmanagement.util;

import com.example.complaintmanagement.model.User;
import jakarta.servlet.http.HttpSession;

public class SessionUtil {

    public static User getLoggedInUser(HttpSession session) {
        return (User) session.getAttribute("loggedInUser");
    }

    public static boolean isAdmin(HttpSession session) {
        User user = getLoggedInUser(session);
        return user != null && "ADMIN".equals(user.getRole());
    }

    public static boolean isUser(HttpSession session) {
        User user = getLoggedInUser(session);
        return user != null && "USER".equals(user.getRole());
    }
}
