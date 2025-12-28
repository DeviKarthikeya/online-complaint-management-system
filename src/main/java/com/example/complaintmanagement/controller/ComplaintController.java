package com.example.complaintmanagement.controller;

import com.example.complaintmanagement.model.Complaint;
import com.example.complaintmanagement.service.ComplaintService;
import com.example.complaintmanagement.util.SessionUtil;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletResponse;
import java.io.PrintWriter;


import java.util.List;

@RestController
@RequestMapping("/api/admin/complaints")
public class ComplaintController {

    private final ComplaintService complaintService;

    public ComplaintController(ComplaintService complaintService) {
        this.complaintService = complaintService;
    }

    // ✅ ADMIN: Get all complaints
    @GetMapping
    public List<Complaint> getAllComplaints(HttpSession session) {

        if (!SessionUtil.isAdmin(session)) {
            throw new RuntimeException("Unauthorized access");
        }

        return complaintService.getAllComplaints();
    }

    // ✅ ADMIN: Filter complaints by status
    @GetMapping("/status/{status}")
    public List<Complaint> getComplaintsByStatus(
            @PathVariable String status,
            HttpSession session) {

        if (!SessionUtil.isAdmin(session)) {
            throw new RuntimeException("Unauthorized access");
        }

        return complaintService.getComplaintsByStatus(status);
    }

    // ✅ ADMIN: Reply to a complaint
    @PostMapping("/{id}/reply")
    public Complaint replyToComplaint(
            @PathVariable Long id,
            @RequestParam String reply,
            HttpSession session) {

        if (!SessionUtil.isAdmin(session)) {
            throw new RuntimeException("Unauthorized access");
        }

        return complaintService.replyToComplaint(id, reply);
    }

    // ✅ USER: Close complaint
    @PostMapping("/{id}/close")
    public Complaint closeComplaint(
            @PathVariable Long id,
            HttpSession session) {

        if (!SessionUtil.isUser(session)) {
            throw new RuntimeException("Unauthorized access");
        }

        return complaintService.closeComplaint(id);
    }

    // ✅ USER: Reopen complaint
    @PostMapping("/{id}/reopen")
    public Complaint reopenComplaint(
            @PathVariable Long id,
            HttpSession session) {

        if (!SessionUtil.isUser(session)) {
            throw new RuntimeException("Unauthorized access");
        }

        return complaintService.reopenComplaint(id);
    }

    // ADMIN: Close complaint
    @PostMapping("/{id}/admin-close")
    public Complaint adminCloseComplaint(
            @PathVariable Long id,
            HttpSession session) {

        if (!SessionUtil.isAdmin(session)) {
            throw new RuntimeException("Unauthorized access");
        }

        return complaintService.adminCloseComplaint(id);
    }

    @GetMapping("/export")
    public void exportComplaintsToCSV(
            HttpSession session,
            HttpServletResponse response) {

        if (!SessionUtil.isAdmin(session)) {
            throw new RuntimeException("Unauthorized");
        }

        response.setContentType("text/csv");
        response.setHeader("Content-Disposition",
                "attachment; filename=complaints.csv");

        try {
            PrintWriter writer = response.getWriter();

            writer.println("ID,User,Description,Status");

            for (Complaint c : complaintService.getAllComplaints()) {
                writer.println(
                        c.getId() + "," +
                                (c.getUser() != null ? c.getUser().getUsername() : "N/A") + "," +
                                c.getDescription().replace(",", " ") + "," +
                                c.getStatus()
                );
            }

            writer.flush();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


}
