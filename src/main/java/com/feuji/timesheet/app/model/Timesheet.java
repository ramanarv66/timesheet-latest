//package com.feuji.timesheet.app.model;
//
//import com.feuji.timesheet.app.entity.Task;
//import com.feuji.timesheet.app.entity.TimesheetStatus;
//import com.feuji.timesheet.app.entity.User;
//import jakarta.persistence.*;
//import lombok.Data;
//
//import java.time.LocalDate;
//
//@Data
//@Entity
//@Table(name = "timesheet")
//public class Timesheet {
//
////    @Id
////    @GeneratedValue(strategy = GenerationType.IDENTITY)
////    private Long id;
////
////    @Column(nullable = false)
////    private Long userId;
////
////    @Column(nullable = false)
////    private String week;
////
////    @Column(nullable = false)
////    private LocalDate date;
////
////    @Column(nullable = false)
////    private int hoursLogged;
////
////    @Column(nullable = false)
////    private String status;
//
//    // Getters and setters
//
//    @Id
//    @GeneratedValue(strategy = GenerationType.IDENTITY)
//    private Long timesheetId;
//
//    @ManyToOne
//    @JoinColumn(name = "user_id")
//    private User user;
//
//    @ManyToOne
//    @JoinColumn(name = "task_id")
//    private Task task;
//
//    private LocalDate date;
//    private float hoursLogged;
//
//    @Enumerated(EnumType.STRING)
//    private TimesheetStatus status;
//
//}