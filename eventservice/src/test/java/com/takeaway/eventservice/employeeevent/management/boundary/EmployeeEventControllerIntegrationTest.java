package com.takeaway.eventservice.employeeevent.management.boundary;

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

import static org.hamcrest.Matchers.*;
import static org.hamcrest.core.Is.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * User: StMinko
 * Date: 13.05.2019
 * Time: 12:09
 * <p/>
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
        @DisplayName("GET: 'http://.../events/{uuid}' returns OK and an list ")
        void givenEmployeeVents_whenFindByUuid_thenStatus200AndContent() throws Exception
        {
            // Arrange
            String uuid = UUID.randomUUID()
                              .toString();
            int expectedEventCount = RandomUtils.nextInt(10, 20);
            receiveRandomMessageFor(uuid, expectedEventCount);

            String uri = String.format("%s/{uuid}", EmployeeEventController.BASE_URI);

            // Act / Assert
            mockMvc.perform(get(uri, uuid))
                   .andExpect(status().isOk())
                   .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8))
                   .andExpect(jsonPath("$", notNullValue()))
                   .andExpect(jsonPath("$.content", hasSize(expectedEventCount)));
        }

        @Test
        @DisplayName("GET: 'http://.../events/{uuid}' returns 400 for blank uuid ")
        void givenBlankUuid_whenFindByUuid_thenStatus400() throws Exception
        {
            // Arrange
            String blankUuid = " ";
            String uri = String.format("%s/{uuid}", EmployeeEventController.BASE_URI);

            // Act / Assert
            mockMvc.perform(get(uri, blankUuid))
                   .andExpect(status().isBadRequest())
                   .andExpect(jsonPath("$.errorMessage", containsString("must not be blank")))
                   .andExpect(jsonPath("$.time", notNullValue()))
                   .andExpect(jsonPath("$.constraintViolations", notNullValue()))
                   .andExpect(jsonPath("$.constraintViolations", hasSize(1)))
                   .andExpect(jsonPath("$.httpStatus", is(400)));
        }
    }

    // ============================  End of class  ===========================
}
