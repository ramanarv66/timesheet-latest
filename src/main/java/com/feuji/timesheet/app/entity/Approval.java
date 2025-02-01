//package com.feuji.timesheet.app.entity;
//
//import jakarta.persistence.*;
//import lombok.Data;
//
//import java.time.LocalDate;
//
//@Data
//@Entity
//public class Approval {
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long approvalId;
//
//    @OneToOne
//    @JoinColumn(name = "timesheet_id")
//    private Timesheet timesheet;
//
//    @ManyToOne
//    @JoinColumn(name = "approved_by")
//    private User approvedBy;
//
//    @Enumerated(EnumType.STRING)
//    private ApprovalStatus status;
//
//    private String comments;
//    private LocalDate approvedDate;
//
//    // Getters and setters
//}
//
