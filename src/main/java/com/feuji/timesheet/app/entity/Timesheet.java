package com.feuji.timesheet.app.entity;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data

@Entity
public class Timesheet {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "timesheet_id", nullable = false, updatable = false)
    private Long timesheetId;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String week;

    @Column(nullable = false)
    private LocalDate date;

    @Column(nullable = false)
    private float hoursLogged;

    @Enumerated(EnumType.STRING)
    private TimesheetStatus status;

    @Column(nullable = false)
    private String day;
    @Column(nullable = false)
    private Integer monthNumber;



    // Getters and setters
}

