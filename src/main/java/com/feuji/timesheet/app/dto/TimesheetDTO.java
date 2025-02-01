package com.feuji.timesheet.app.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;

import java.time.LocalDate;

@Data
public class TimesheetDTO {
    private String userId;
    private String week;
    @JsonFormat(pattern = "d/M/yyyy")
    private LocalDate date;
    private Integer hoursLogged;
    private String day;
    private int monthNumber;

}
