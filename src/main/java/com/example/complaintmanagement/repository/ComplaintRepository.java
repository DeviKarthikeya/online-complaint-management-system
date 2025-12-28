package com.example.complaintmanagement.repository;

import com.example.complaintmanagement.model.Complaint;
import com.example.complaintmanagement.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ComplaintRepository extends JpaRepository<Complaint, Long> {

    List<Complaint> findByUser(User user);

    List<Complaint> findByStatus(String status);

    List<Complaint> findByUserUsername(String username);

    List<Complaint> findByStatusAndUserUsername(String status, String username);

}
