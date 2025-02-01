package com.feuji.timesheet.app.controller;

import com.feuji.timesheet.app.dto.TimesheetDTO;
import com.feuji.timesheet.app.entity.Timesheet;
import com.feuji.timesheet.app.repository.TimesheetRepository;
import com.feuji.timesheet.app.service.TimesheetService;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.List;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/report")
public class ReportController {

    @Autowired
    private TimesheetRepository timesheetRepository;

    @Autowired
    private  TimesheetService timesheetService;

    @GetMapping("{userId}")
    public ResponseEntity<byte[]> downloadTimesheet(@PathVariable String userId) throws IOException {
        List<Timesheet> timesheets = timesheetRepository.findByUserId(userId);

        if (timesheets.isEmpty()) {
            return ResponseEntity.notFound().build();
        }



        byte[] excelBytes = timesheetService.generateExcelForSingleUser(timesheets);

        // Set response headers
        HttpHeaders headersResponse = new HttpHeaders();
        headersResponse.setContentType(MediaType.APPLICATION_OCTET_STREAM);
        headersResponse.setContentDispositionFormData("attachment", "timesheets.xlsx");

        return ResponseEntity.ok()
                .headers(headersResponse)
                .body(excelBytes);
    }

    @GetMapping("/timesheets")
    public ResponseEntity<byte[]> exportTimesheets() throws IOException {
        // Mock data (you can fetch from database instead)
        Map<String, List<TimesheetDTO>> timesheetData = new HashMap<>();

        // Adding sample data for users

        List<Timesheet> allTimeSheets = timesheetRepository.findAllTimesheetsForAllUsers();
        Map<String, List<Timesheet>> all = allTimeSheets.stream().collect(Collectors.groupingBy(Timesheet::getUserId));
        System.out.println(all);
        byte[] excelBytes = timesheetService.generateExcel(all);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=timesheets.xlsx")
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(excelBytes);
    }
    }
