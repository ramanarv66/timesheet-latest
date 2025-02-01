package com.feuji.timesheet.app.service;

import com.feuji.timesheet.app.entity.Timesheet;
import com.feuji.timesheet.app.repository.TimesheetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TimesheetService1 {

    private final TimesheetRepository timesheetRepository;

    public TimesheetService1(TimesheetRepository timesheetRepository) {
        this.timesheetRepository = timesheetRepository;
    }

    public Timesheet saveTimesheet(Timesheet timesheet) {
        return timesheetRepository.save(timesheet);
    }

    public List<Timesheet> getAllTimesheets() {
        return timesheetRepository.findAll();
    }
}