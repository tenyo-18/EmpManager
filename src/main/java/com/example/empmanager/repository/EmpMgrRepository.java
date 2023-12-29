package com.example.empmanager.repository;

import com.example.empmanager.model.Employee;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EmpMgrRepository extends JpaRepository<Employee, Long> {
  Employee findByName(String name);

  @Query("SELECT e from Employee e LEFT JOIN e.directReports s WHERE s.name = :employeeName")
  Employee findManagerByName(@Param("employeeName") String employeeName);
}
