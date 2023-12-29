package com.example.empmanager.model;

import java.util.ArrayList;
import java.util.List;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinTable;
import javax.persistence.OneToMany;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
@Entity
public class Employee {

  @Id
  @GeneratedValue(strategy = GenerationType.AUTO)
  Long id;

  @Column(nullable = false)
  String name;

  @JoinTable(name = "employee_direct_reports")
  @OneToMany(
      fetch = FetchType.EAGER,
      cascade = {CascadeType.PERSIST})
  List<Employee> directReports = new ArrayList<Employee>();
}
