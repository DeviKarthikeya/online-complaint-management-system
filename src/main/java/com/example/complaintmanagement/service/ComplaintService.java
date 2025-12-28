package com.example.complaintmanagement.service;

import com.example.complaintmanagement.model.Complaint;
import com.example.complaintmanagement.model.User;
import com.example.complaintmanagement.repository.ComplaintRepository;
import org.springframework.stereotype.Service;

import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import java.util.List;

@Service
public class ComplaintService {

    private final ComplaintRepository complaintRepository;

    public ComplaintService(ComplaintRepository complaintRepository) {
        this.complaintRepository = complaintRepository;
    }

    // User creates complaint
    public Complaint createComplaint(String description, User user) {
        Complaint complaint = new Complaint();
        complaint.setDescription(description);
        complaint.setStatus("OPEN");
        complaint.setUser(user);
        return complaintRepository.save(complaint);
    }

    // User views their complaints
    public List<Complaint> getComplaintsByUser(User user) {
        return complaintRepository.findByUser(user);
    }

    // Admin views all complaints
    public List<Complaint> getAllComplaints() {
        return complaintRepository.findAll();
    }

    // Admin filters complaints by status
    public List<Complaint> getComplaintsByStatus(String status) {
        return complaintRepository.findByStatus(status);
    }

    // Admin replies to complaint
    public Complaint replyToComplaint(Long complaintId, String reply) {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));

        DateTimeFormatter formatter =
                DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        String timestamp = LocalDateTime.now().format(formatter);

        String newReply =
                "[" + timestamp + "] ADMIN: " + reply;

        String oldReplies = complaint.getAdminReplyHistory();

        if (oldReplies == null || oldReplies.isEmpty()) {
            complaint.setAdminReplyHistory(newReply);
        } else {
            // 🔑 Use delimiter between replies
            complaint.setAdminReplyHistory(oldReplies + "|||" + newReply);
        }

        complaint.setStatus("IN_PROGRESS");
        return complaintRepository.save(complaint);
    }


    // User closes complaint
    public Complaint closeComplaint(Long complaintId) {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));
        complaint.setStatus("CLOSED");
        return complaintRepository.save(complaint);
    }

    // User reopens complaint
    public Complaint reopenComplaint(Long complaintId) {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));
        complaint.setStatus("OPEN");
        return complaintRepository.save(complaint);
    }

    // Admin closes complaint
    public Complaint adminCloseComplaint(Long complaintId) {
        Complaint complaint = complaintRepository.findById(complaintId)
                .orElseThrow(() -> new RuntimeException("Complaint not found"));
        complaint.setStatus("CLOSED");
        return complaintRepository.save(complaint);
    }

    public List<Complaint> filterComplaints(String status, String username) {

        if ((status == null || status.isEmpty())
                && (username == null || username.isEmpty())) {
            return complaintRepository.findAll();
        }

        if (status != null && !status.isEmpty()
                && (username == null || username.isEmpty())) {
            return complaintRepository.findByStatus(status);
        }

        if ((status == null || status.isEmpty())
                && username != null && !username.isEmpty()) {
            return complaintRepository.findByUserUsername(username);
        }

        return complaintRepository.findByStatusAndUserUsername(status, username);
    }

    public void writeUserComplaintsToCsv(User user, PrintWriter writer) {

        List<Complaint> complaints = complaintRepository.findByUser(user);

        writer.println("ID,Description,Status");

        for (Complaint c : complaints) {
            writer.println(
                    c.getId() + "," +
                            c.getDescription().replace(",", " ") + "," +
                            c.getStatus()
            );
        }

        writer.flush();
    }



}
