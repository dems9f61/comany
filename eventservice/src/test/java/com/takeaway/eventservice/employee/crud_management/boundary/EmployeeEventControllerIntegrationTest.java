package com.takeaway.eventservice.employee.crud_management.boundary;

import com.takeaway.eventservice.IntegrationTestSuite;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.notNullValue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * User: StMinko Date: 13.05.2019 Time: 12:09
 *
 * <p>
 */
@AutoConfigureMockMvc
class EmployeeEventControllerIntegrationTest extends IntegrationTestSuite
{
  // =========================== Class Variables ===========================
  // =============================  Variables  =============================

  @Autowired
  private MockMvc mockMvc;

  // ============================  Constructors  ===========================
  // ===========================  public  Methods  =========================
  // =================  protected/package local  Methods ===================
  // ===========================  private  Methods  ========================
  // ============================  Inner Classes  ==========================

  @Nested
  @DisplayName("when access")
  class WhenAccess
  {
    @Test
    @DisplayName("GET: 'http://.../events/{employeeId}' returns OK and an list ")
    void givenEmployeeVents_whenFindByEmployeeId_thenStatus200AndContent() throws Exception
    {
      // Arrange
      UUID employeeId = UUID.randomUUID();
      int expectedEventCount = RandomUtils.nextInt(10, 20);
      receiveRandomMessageFor(employeeId, expectedEventCount);

      String uri = String.format("%s/{employeeId}", EmployeeEventController.BASE_URI);

      // Act / Assert
      mockMvc
          .perform(get(uri, employeeId))
          .andExpect(status().isOk())
          .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
          .andExpect(jsonPath("$", notNullValue()))
          .andExpect(jsonPath("$.content", hasSize(expectedEventCount)));
    }
  }

  // ============================  End of class  ===========================
}
