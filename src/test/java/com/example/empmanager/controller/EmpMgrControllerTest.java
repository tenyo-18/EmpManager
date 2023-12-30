package com.example.empmanager.controller;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.example.empmanager.EmpManagerApplication;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.Map;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;

@ExtendWith(SpringExtension.class)
@SpringBootTest(classes = EmpManagerApplication.class)
@AutoConfigureMockMvc(addFilters = false)
public class EmpMgrControllerTest {

  @Autowired private MockMvc mockMvc;

  private String employeesResource = "/api/employees";
  private ObjectMapper mapper = new ObjectMapper();

  @Test
  void testSaveHierarchy() throws Exception {

    Map<String, String> employees =
        Map.of(
            "Jack", "Jamie",
            "Jamie", "Tim",
            "Tim", "Sarah");

    mockMvc
        .perform(
            post(employeesResource)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(employees))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isCreated())
        .andExpect(content().json("{\"Sarah\":{\"Tim\":{\"Jamie\":{\"Jack\":{}}}}}"))
        .andDo(MockMvcResultHandlers.print());
  }

  @Test
  void testSaveHierarchyNoCEO() throws Exception {

    Map<String, String> employees =
        Map.of(
            "Jack", "Jamie",
            "Jamie", "Tim",
            "Tim", "Sarah",
            "Sarah", "Jack");

    mockMvc
        .perform(
            post(employeesResource)
                .accept(MediaType.APPLICATION_JSON)
                .content(mapper.writeValueAsString(employees))
                .contentType(MediaType.APPLICATION_JSON))
        .andExpect(status().isNotFound())
        .andExpect(
            jsonPath("$.errorMessage")
                .value("No CEO Found for this hierarchy in the available data set"))
        .andDo(MockMvcResultHandlers.print());
  }
}
