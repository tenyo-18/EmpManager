package com.example.empmanager.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

import com.example.empmanager.model.Employee;
import com.example.empmanager.repository.EmpMgrRepository;
import java.util.HashMap;
import java.util.Map;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

public class EmpMgrServiceTest {

  @Mock EmpMgrRepository empMgrRepository;
  private EmpMgrService empMgrService;

  @BeforeEach
  void setUp() {
    MockitoAnnotations.initMocks(this);
    doNothing().when(empMgrRepository).deleteAll();
    when(empMgrRepository.save(any())).thenReturn(new Employee());
    Employee employeeJamie = new Employee();
    employeeJamie.setName("Jamie");
    Employee employeeSarah = new Employee();
    employeeSarah.setName("Sarah");
    when(empMgrRepository.findByName("Jamie")).thenReturn(employeeJamie);
    when(empMgrRepository.findManagerByName("Jamie")).thenReturn(employeeSarah);
    empMgrService = new EmpMgrService(empMgrRepository);
  }

  @Test
  public void testSaveHierarchy() throws Exception {
    Map<String, String> inputHierarchy = new HashMap<>();
    inputHierarchy.put("Jack", "Jamie");
    inputHierarchy.put("Tim", "Jamie");
    inputHierarchy.put("Jamie", "Sarah");
    Map<String, Map<String, ? extends Object>> result = empMgrService.saveHierarchy(inputHierarchy);
    Map<String, ? extends Object> innerHierarchy = result.get("Sarah");
    assertTrue(result.containsKey("Sarah"));
    assertTrue(innerHierarchy.containsKey("Jamie"));
    assertEquals("{Sarah={Jamie={Tim={}, Jack={}}}}", result.toString());
  }

  @Test
  public void testFindEmployeeManagers() throws Exception {
    Map<String, String> result = empMgrService.findEmployeeManagers("Jamie", 1);
    assertEquals(result.get("Jamie"), "Sarah");
  }
}
