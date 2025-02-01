package com.feuji.timesheet.app.service;

import com.feuji.timesheet.app.dto.TimesheetDTO;
import com.feuji.timesheet.app.entity.Timesheet;
import com.feuji.timesheet.app.entity.TimesheetStatus;
import com.feuji.timesheet.app.repository.TimesheetRepository;
import org.apache.poi.ss.usermodel.Workbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.stereotype.Service;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class TimesheetService {
    @Autowired
    private TimesheetRepository timesheetRepository;


    public void saveTimesheets(List<TimesheetDTO> timesheets) {
        for (TimesheetDTO timesheetDTO : timesheets) {
            Timesheet entity = new Timesheet();
            entity.setUserId(timesheetDTO.getUserId()); // Set the User entity
            entity.setDate(timesheetDTO.getDate());
            entity.setHoursLogged(timesheetDTO.getHoursLogged());
            entity.setWeek(timesheetDTO.getWeek());
            entity.setStatus(TimesheetStatus.SUBMITTED); // Default status
            entity.setDay(timesheetDTO.getDay());
            entity.setMonthNumber(timesheetDTO.getMonthNumber());
            // Save the Timesheet entity
            timesheetRepository.save(entity);
        }


    }
//        public List<Timesheet> getUserTimesheets(Long userId) {
//        return timesheetRepository.findByUserUserId(userId);
//    }

    public List<Timesheet> getTimesheetsByUserId(String userId) {
        return timesheetRepository.findByUserId(userId);
    }
    public List<Timesheet> findAllTimesheetsForAllUsers() {
        List<Timesheet> allTimeSheets = timesheetRepository.findAllTimesheetsForAllUsers();
        Map<String, List<Timesheet>> all = allTimeSheets.stream().collect(Collectors.groupingBy(Timesheet::getUserId));
        System.out.println(all);
        return allTimeSheets;
    }
    public byte[] generateExcelForSingleUser(List<Timesheet> timesheets) throws IOException {
        // Create the Excel file
        Workbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet("Timesheets");

        // Create a date format style for the date column
        CreationHelper creationHelper = workbook.getCreationHelper();
        CellStyle dateCellStyle = workbook.createCellStyle();
        dateCellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("yyyy-MM-dd"));

        // Add header row
        Row headerRow = sheet.createRow(0);
        String[] headers = {"Timesheet ID", "User ID", "Week", "Date", "Day", "Hours Logged", "Status"};
        CellStyle headerStyle = workbook.createCellStyle();
        headerStyle.setFillForegroundColor(IndexedColors.MAROON.getIndex());
        headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
        Font headerFont = workbook.createFont();
        headerFont.setColor(IndexedColors.WHITE.getIndex());
        headerStyle.setFont(headerFont);

        for (int i = 0; i < headers.length; i++) {
            Cell cell = headerRow.createCell(i);
            cell.setCellValue(headers[i]);
            cell.setCellStyle(headerStyle);
        }

        // Initialize variables
        int totalWorkableHours = 0;
        int totalLeaves = 0;

        // Add data rows
        int rowIndex = 1;
        for (Timesheet timesheet : timesheets) {
            Row row = sheet.createRow(rowIndex++);
            row.createCell(0).setCellValue(timesheet.getTimesheetId());
            row.createCell(1).setCellValue(timesheet.getUserId());
            row.createCell(2).setCellValue(timesheet.getWeek());

            // Set the date value
            LocalDate localDate = timesheet.getDate(); // Assuming it's already LocalDate
            java.util.Date date = java.sql.Date.valueOf(localDate);
            Cell dateCell = row.createCell(3);
            dateCell.setCellValue(date);
            dateCell.setCellStyle(dateCellStyle);

            // Add day value
            DayOfWeek dayOfWeek = localDate.getDayOfWeek();
            row.createCell(4).setCellValue(dayOfWeek.toString());

            // Add hoursLogged (numeric column)
            Cell hoursLoggedCell = row.createCell(5);
            hoursLoggedCell.setCellValue(timesheet.getHoursLogged());

            row.createCell(6).setCellValue(timesheet.getStatus().toString());

            // Calculate total workable hours and leaves
            if (dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY) {
                totalWorkableHours += 8; // Add 8 hours for each working day
                if (timesheet.getHoursLogged() == 0) {
                    totalLeaves += 8; // Count this as leave
                }
            }
        }

        // Calculate the final result
        int finalHours = totalWorkableHours - totalLeaves;

        // Add a footer row to display totals
        Row footerRow = sheet.createRow(rowIndex++);
        CellStyle footerStyle = workbook.createCellStyle();
        footerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
        footerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

        Cell footerLabelCell = footerRow.createCell(0);
        footerLabelCell.setCellValue("Total Workable Hours");
        footerLabelCell.setCellStyle(footerStyle);

        Cell totalWorkableHoursCell = footerRow.createCell(1);
        totalWorkableHoursCell.setCellValue(totalWorkableHours);
        totalWorkableHoursCell.setCellStyle(footerStyle);

        Cell leavesLabelCell = footerRow.createCell(2);
        leavesLabelCell.setCellValue("Total Leaves (0 Hours Logged)");
        leavesLabelCell.setCellStyle(footerStyle);

        Cell totalLeavesCell = footerRow.createCell(3);
        totalLeavesCell.setCellValue(totalLeaves);
        totalLeavesCell.setCellStyle(footerStyle);

        Cell finalLabelCell = footerRow.createCell(4);
        finalLabelCell.setCellValue("Final Hours");
        finalLabelCell.setCellStyle(footerStyle);

        Cell finalHoursCell = footerRow.createCell(5);
        finalHoursCell.setCellValue(finalHours);
        finalHoursCell.setCellStyle(footerStyle);

        // Auto-size columns
        for (int i = 0; i < headers.length; i++) {
            sheet.autoSizeColumn(i);
        }

        // Write to byte array
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        workbook.write(outputStream);
        workbook.close();
        return outputStream.toByteArray();
    }
    public byte[] generateExcel(Map<String, List<Timesheet>> timesheetData) throws IOException {
        try (Workbook workbook = new XSSFWorkbook(); ByteArrayOutputStream outputStream = new ByteArrayOutputStream()) {

            // Iterate over users (each user will get a separate sheet)
            for (Map.Entry<String, List<Timesheet>> entry : timesheetData.entrySet()) {
                String userId = entry.getKey();
                List<Timesheet> timesheets = entry.getValue();
                CreationHelper creationHelper = workbook.getCreationHelper();
                CellStyle dateCellStyle = workbook.createCellStyle();
                dateCellStyle.setDataFormat(creationHelper.createDataFormat().getFormat("yyyy-MM-dd"));
                Sheet sheet = workbook.createSheet(userId);

                // Create header row
                Row headerRow = sheet.createRow(0);
                String[] headers = {"Timesheet ID", "User ID", "Week", "Date", "Day", "Hours Logged", "Status"};

                for (int i = 0; i < headers.length; i++) {
                    Cell cell = headerRow.createCell(i);
                    cell.setCellValue(headers[i]);
                    CellStyle headerStyle = workbook.createCellStyle();
                    Font font = workbook.createFont();
                    font.setBold(true);
                    headerStyle.setFont(font);
                    cell.setCellStyle(headerStyle);


                    headerStyle.setFillForegroundColor(IndexedColors.MAROON.getIndex());
                    headerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
                    Font headerFont = workbook.createFont();
                    headerFont.setColor(IndexedColors.WHITE.getIndex());
                    headerStyle.setFont(headerFont);




                }

                // Populate data rows
                int rowIndex = 1;
                int totalWorkableHours = 0;
                int totalLeaves = 0;

                for (Timesheet timesheet : timesheets) {
                    Row row = sheet.createRow(rowIndex++);
                    row.createCell(0).setCellValue(timesheet.getTimesheetId());
                    row.createCell(1).setCellValue(timesheet.getUserId());
                    row.createCell(2).setCellValue(timesheet.getWeek());

                    // Set the date value
                    LocalDate localDate = timesheet.getDate(); // Assuming it's already LocalDate
                    java.util.Date date = java.sql.Date.valueOf(localDate);
                    Cell dateCell = row.createCell(3);
                    dateCell.setCellValue(date);
                    dateCell.setCellStyle(dateCellStyle);

                    // Add day value
                    DayOfWeek dayOfWeek = localDate.getDayOfWeek();
                    row.createCell(4).setCellValue(dayOfWeek.toString());

                    // Add hoursLogged (numeric column)
                    Cell hoursLoggedCell = row.createCell(5);
                    hoursLoggedCell.setCellValue(timesheet.getHoursLogged());

                    row.createCell(6).setCellValue(timesheet.getStatus().toString());

                    // Calculate total workable hours and leaves
                    if (dayOfWeek != DayOfWeek.SATURDAY && dayOfWeek != DayOfWeek.SUNDAY) {
                        totalWorkableHours += 8; // Add 8 hours for each working day
                        if (timesheet.getHoursLogged() == 0) {
                            totalLeaves += 8; // Count this as leave
                        }
                    }
                }
                // Calculate the final result
                int finalHours = totalWorkableHours - totalLeaves;

                // Add a footer row to display totals
                Row footerRow = sheet.createRow(rowIndex++);
                CellStyle footerStyle = workbook.createCellStyle();
                footerStyle.setFillForegroundColor(IndexedColors.GREY_25_PERCENT.getIndex());
                footerStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);

                Cell footerLabelCell = footerRow.createCell(0);
                footerLabelCell.setCellValue("Total Workable Hours");
                footerLabelCell.setCellStyle(footerStyle);

                Cell totalWorkableHoursCell = footerRow.createCell(1);
                totalWorkableHoursCell.setCellValue(totalWorkableHours);
                totalWorkableHoursCell.setCellStyle(footerStyle);

                Cell leavesLabelCell = footerRow.createCell(2);
                leavesLabelCell.setCellValue("Total Leaves (0 Hours Logged)");
                leavesLabelCell.setCellStyle(footerStyle);

                Cell totalLeavesCell = footerRow.createCell(3);
                totalLeavesCell.setCellValue(totalLeaves);
                totalLeavesCell.setCellStyle(footerStyle);

                Cell finalLabelCell = footerRow.createCell(4);
                finalLabelCell.setCellValue("Final Hours");
                finalLabelCell.setCellStyle(footerStyle);

                Cell finalHoursCell = footerRow.createCell(5);
                finalHoursCell.setCellValue(finalHours);
                finalHoursCell.setCellStyle(footerStyle);
                // Auto-size columns
                for (int i = 0; i < headers.length; i++) {
                    sheet.autoSizeColumn(i);
                }
            }

            // Write data to output stream
            workbook.write(outputStream);
            workbook.close();
            return outputStream.toByteArray();
        }
    }


}
