package com.feuji.timesheet.app.repository;
import com.feuji.timesheet.app.entity.Timesheet;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

//public interface TimesheetRepository extends JpaRepository<Timesheet, Long> {
//   // Optional<Timesheet> findByUserIdAndMonth(String userId, String month);
//}

@Repository
public interface TimesheetRepository extends JpaRepository<Timesheet, Long> {
    //List<Timesheet> findByUserUserId(Long userId);

    List<Timesheet> findByUserId(String userId);
    @Query("SELECT t FROM Timesheet t ORDER BY t.userId, t.date")
    List<Timesheet> findAllTimesheetsForAllUsers();
}