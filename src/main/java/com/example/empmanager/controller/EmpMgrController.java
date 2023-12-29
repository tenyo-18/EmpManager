package com.example.empmanager.controller;

import com.example.empmanager.exception.DataNotFoundException;
import com.example.empmanager.service.EmpMgrService;
import java.util.Map;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/employees")
public class EmpMgrController {

  @Autowired private EmpMgrService empMgrService;

  @Autowired
  public EmpMgrController(EmpMgrService empMgrService) {
    this.empMgrService = empMgrService;
  }

  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  @ResponseBody
  public Map<String, Map<String, ? extends Object>> saveEmpHierarchy(
      @RequestBody Map<String, String> input) throws DataNotFoundException {
    if (input.isEmpty()) {
      throw new DataNotFoundException("Invalid Input Hierarchy");
    }
    return empMgrService.saveHierarchy(input);
  }

  @GetMapping("/supervisors")
  public Map<String, String> findManagers(@RequestParam String name, @RequestParam int levels)
      throws Exception {
    return empMgrService.findEmployeeManagers(name, levels);
  }
}
