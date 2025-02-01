package com.feuji.timesheet.app.controller;

import com.feuji.timesheet.app.dto.TimesheetDTO;
import com.feuji.timesheet.app.entity.Timesheet;
import com.feuji.timesheet.app.entity.TimesheetStatus;
import com.feuji.timesheet.app.service.TimesheetService;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/api/timesheet")
public class TimesheetController {

    private final TimesheetService timesheetService;

    public TimesheetController(TimesheetService timesheetService) {
        this.timesheetService = timesheetService;
    }

//    @PostMapping("/save")
//    public ResponseEntity<String> saveTimesheet(@RequestBody List<Timesheet> timesheet) {
//        timesheet.forEach(timesheetService::saveTimesheet);
//        return ResponseEntity.ok("Timesheet saved successfully!");
//    }

    @PostMapping("/save")
    public ResponseEntity<String> saveTimesheets(@RequestBody List<TimesheetDTO> timesheets) {
            timesheetService.saveTimesheets(timesheets);
        return ResponseEntity.ok("Timesheets saved successfully!");
    }


    @GetMapping("{userId}")
    public ResponseEntity<List<Timesheet>> getTimesheetsByUserId(@PathVariable String userId) {
        return ResponseEntity.ok(timesheetService.getTimesheetsByUserId(userId));
    }
    @GetMapping("/all")
    public ResponseEntity<List<Timesheet>>getAllTimesheets(){
        return ResponseEntity.ok(timesheetService.findAllTimesheetsForAllUsers());
    }

    @PostMapping("/hello")
    public String helloWorld(){
        return  "hello";
    }
}
