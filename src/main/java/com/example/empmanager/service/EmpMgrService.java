package com.example.empmanager.service;

import com.example.empmanager.exception.DataNotFoundException;
import com.example.empmanager.model.Employee;
import com.example.empmanager.repository.EmpMgrRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;

@Slf4j
@Service
public class EmpMgrService {

  private EmpMgrRepository empMgrRepository;

  public EmpMgrService(EmpMgrRepository empMgrRepository) {
    this.empMgrRepository = empMgrRepository;
  }

  public Map<String, String> findEmployeeManagers(String employeeName, int levels)
      throws Exception {
    Employee emp = empMgrRepository.findByName(employeeName);
    Map<String, String> managerMap = new HashMap<>();
    if (Objects.isNull(emp)) {
      throw new Exception("Employee not found " + employeeName);
    }

    findManagers(emp.getName(), managerMap, levels);
    return managerMap;
  }

  private void findManagers(String name, Map<String, String> supervisors, int levels) {
    Employee manager = empMgrRepository.findManagerByName(name);
    if (levels == 0 || Objects.isNull(manager)) {
      return;
    }
    supervisors.putIfAbsent(name, manager.getName());
    findManagers(manager.getName(), supervisors, levels - 1);
  }

  public Map<String, Map<String, ? extends Object>> saveHierarchy(Map<String, String> input)
      throws DataNotFoundException {
    LinkedMultiValueMap<String, String> multiValueMap = new LinkedMultiValueMap();
    List<String> employeeList = new ArrayList<>();
    List<String> managerList = new ArrayList<>();
    try {
      input
          .entrySet()
          .forEach(
              entry -> {
                multiValueMap.add(entry.getValue(), entry.getKey());
                employeeList.add(entry.getKey());
                managerList.add(entry.getValue());
              });
      String ceo = findCEO(employeeList, managerList);
      Employee ceoEmployee = navigateHierarchy(multiValueMap, ceo);
      empMgrRepository.deleteAll();
      empMgrRepository.save(ceoEmployee);
      return mapifyResponse(ceoEmployee);

    } catch (DataNotFoundException e) {
      log.error("No CEO Found for this hierarchy in the available data set", e);
      throw new DataNotFoundException("No CEO Found for this hierarchy in the available data set");
    }
  }

  private Map<String, Map<String, ? extends Object>> mapifyResponse(Employee employee) {
    return Map.of(employee.getName(), asMap(employee.getDirectReports()));
  }

  private Map<String, Map<String, ? extends Object>> asMap(List<Employee> employees) {
    Map<String, Map<String, ? extends Object>> innerMap = new HashMap<>();

    employees.stream()
        .forEach(
            employee -> {
              innerMap.put(employee.getName(), asMap(employee.getDirectReports()));
            });
    return innerMap;
  }

  private Employee navigateHierarchy(Map<String, List<String>> hierarchy, String manager) {
    Employee employee = new Employee();
    employee.setName(manager);
    employee.setDirectReports(getAllReporteesRecursively(hierarchy, manager));
    return employee;
  }

  private List<Employee> getAllReporteesRecursively(
      Map<String, List<String>> hierarchy, String manager) {
    List<String> employeeList = new ArrayList<>();
    if (hierarchy.containsKey(manager)) {
      employeeList = hierarchy.get(manager);
    }
    return employeeList.stream()
        .map(emp -> navigateHierarchy(hierarchy, emp))
        .collect(Collectors.toList());
  }

  private String findCEO(List<String> employees, List<String> managers)
      throws DataNotFoundException {
    managers.removeAll(employees);
    if (managers.isEmpty()) {
      throw new DataNotFoundException("No CEO Found for this hieararchy");
    }
    return managers.get(0);
  }
}
