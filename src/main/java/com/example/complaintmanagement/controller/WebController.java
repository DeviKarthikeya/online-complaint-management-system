package com.example.complaintmanagement.controller;

import com.example.complaintmanagement.model.Complaint;
import com.example.complaintmanagement.model.User;
import com.example.complaintmanagement.service.ComplaintService;
import com.example.complaintmanagement.service.UserService;
import com.example.complaintmanagement.util.SessionUtil;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
public class WebController {

    private final ComplaintService complaintService;
    private final UserService userService;


    public WebController(ComplaintService complaintService, UserService userService) {
        this.complaintService = complaintService;
        this.userService = userService;
    }

    // ================= USER DASHBOARD =================
    @GetMapping("/user/dashboard")
    public String userDashboard(HttpSession session, Model model) {

        if (!SessionUtil.isUser(session)) {
            return "redirect:/login";
        }

        User user = SessionUtil.getLoggedInUser(session);
        model.addAttribute("loggedInUser", user);

        // ✅ STEP 1: create local variable
        List<Complaint> complaints =
                complaintService.getComplaintsByUser(user);

        // ✅ STEP 2: send to UI
        model.addAttribute("complaints", complaints);

        // ✅ STEP 3: now you can safely calculate counts
        long total = complaints.size();
        long open = complaints.stream()
                .filter(c -> "OPEN".equals(c.getStatus()))
                .count();
        long closed = complaints.stream()
                .filter(c -> "CLOSED".equals(c.getStatus()))
                .count();

        model.addAttribute("totalCount", total);
        model.addAttribute("openCount", open);
        model.addAttribute("closedCount", closed);

        return "user-dashboard";
    }


    // ================= ADMIN DASHBOARD =================
    @GetMapping("/admin/dashboard")
    public String adminDashboard(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String username,
            HttpSession session,
            Model model) {

        if (!SessionUtil.isAdmin(session)) {
            return "redirect:/login";
        }

        User admin = SessionUtil.getLoggedInUser(session);
        model.addAttribute("loggedInUser", admin);

        List<Complaint> complaints =
                complaintService.filterComplaints(status, username);

        model.addAttribute("complaints", complaints);
        model.addAttribute("selectedStatus", status);
        model.addAttribute("enteredUsername", username);

        long total = complaints.size();
        long open = complaints.stream().filter(c -> "OPEN".equals(c.getStatus())).count();
        long inProgress = complaints.stream().filter(c -> "IN_PROGRESS".equals(c.getStatus())).count();
        long closed = complaints.stream().filter(c -> "CLOSED".equals(c.getStatus())).count();

        model.addAttribute("totalCount", total);
        model.addAttribute("openCount", open);
        model.addAttribute("inProgressCount", inProgress);
        model.addAttribute("closedCount", closed);


        return "admin-dashboard";
    }

    // ================= USER: CREATE COMPLAINT =================
    @PostMapping("/user/complaint")
    public String createComplaint(
            @RequestParam String description,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        if (!SessionUtil.isUser(session)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Please login first.");
            return "redirect:/login";
        }

        User user = SessionUtil.getLoggedInUser(session);
        complaintService.createComplaint(description, user);

        redirectAttributes.addFlashAttribute(
                "successMessage", "Complaint raised successfully.");

        return "redirect:/user/dashboard";
    }


    // ================= USER: EXPORT USER COMPLAINT =================
    @GetMapping("/user/export")
    public void exportUserComplaints(
            HttpSession session,
            HttpServletResponse response) throws IOException {

        if (!SessionUtil.isUser(session)) {
            response.sendRedirect("/login");
            return;
        }

        User user = SessionUtil.getLoggedInUser(session);

        response.setContentType("text/csv");
        response.setHeader(
                "Content-Disposition",
                "attachment; filename=my_complaints.csv"
        );

        complaintService.writeUserComplaintsToCsv(user, response.getWriter());
    }

    // ================= DELETE ACCOUNT =================
    @PostMapping("/account/delete")
    public String deleteAccount(HttpSession session) {

        User user = SessionUtil.getLoggedInUser(session);
        if (user == null) {
            return "redirect:/login";
        }

        userService.deleteUserAccount(user.getId());

        session.invalidate();
        return "redirect:/";
    }

    @PostMapping("/admin/complaints/{id}/reply")
    public String replyToComplaintUI(
            @PathVariable Long id,
            @RequestParam String reply,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        if (!SessionUtil.isAdmin(session)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Unauthorized action.");
            return "redirect:/login";
        }

        complaintService.replyToComplaint(id, reply);

        redirectAttributes.addFlashAttribute(
                "successMessage", "Reply sent successfully.");

        return "redirect:/admin/dashboard";
    }


    @PostMapping("/admin/complaints/{id}/close")
    public String closeComplaintUI(
            @PathVariable Long id,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        if (!SessionUtil.isAdmin(session)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Unauthorized action.");
            return "redirect:/login";
        }

        complaintService.closeComplaint(id);

        redirectAttributes.addFlashAttribute(
                "successMessage", "Complaint closed.");

        return "redirect:/admin/dashboard";
    }



    @PostMapping("/user/complaints/{id}/close")
    public String closeComplaintUser(
            @PathVariable Long id,
            HttpSession session) {

        if (!SessionUtil.isUser(session)) {
            return "redirect:/login";
        }

        complaintService.closeComplaint(id);
        return "redirect:/user/dashboard";
    }

    @PostMapping("/user/complaints/{id}/reopen")
    public String reopenComplaintByUser(
            @PathVariable Long id,
            HttpSession session,
            RedirectAttributes redirectAttributes) {

        if (!SessionUtil.isUser(session)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Unauthorized action.");
            return "redirect:/login";
        }

        complaintService.reopenComplaint(id);

        redirectAttributes.addFlashAttribute(
                "successMessage", "Complaint reopened.");

        return "redirect:/user/dashboard";
    }

    @GetMapping("/")
    public String homePage(HttpSession session) {

        // If already logged in, redirect to dashboard
        if (SessionUtil.isAdmin(session)) {
            return "redirect:/admin/dashboard";
        }

        if (SessionUtil.isUser(session)) {
            return "redirect:/user/dashboard";
        }

        return "index";
    }

}
