package com.example.empmanager.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.empmanager.EmpManagerApplication;
import com.example.empmanager.model.Employee;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = EmpManagerApplication.class)
public class EmpMgrRepositoryTest {

  @Autowired private EmpMgrRepository empMgrRepository;

  @BeforeEach
  void setUp() {
    empMgrRepository.deleteAll();
  }

  @Test
  void testSaveHierarchy() {

    Employee result = empMgrRepository.save(createEmployee());

    assertEquals("Barbara", result.getName());
  }

  @Test
  void testFindByName() {

    empMgrRepository.save(createEmployee());
    Employee result = empMgrRepository.findByName("Barbara");
    assertEquals("Barbara", result.getName());
  }

  @Test
  void testFindByManager() {

    empMgrRepository.save(createEmployee());
    Employee result = empMgrRepository.findManagerByName("Jamie");
    assertEquals("Barbara", result.getName());
  }

  private Employee createEmployee() {
    Employee employee1 = new Employee();
    employee1.setName("Jamie");

    Employee employee2 = new Employee();
    employee2.setName("Barbara");
    employee2.setDirectReports(List.of(employee1));
    return employee2;
  }
}
