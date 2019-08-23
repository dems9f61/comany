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
            UUID uuid = UUID.randomUUID();
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
        @DisplayName("GET: 'http://.../events/{uuid}' returns 404 for unknown uuid ")
        void givenWrongUuid_whenFindByUuid_thenStatus404() throws Exception
        {
            // Arrange
            UUID wrongUuid = UUID.randomUUID();
            String uri = String.format("%s/{uuid}", EmployeeEventController.BASE_URI);

            // Act / Assert
            mockMvc.perform(get(uri, wrongUuid))
                   .andExpect(status().isNotFound())
                   .andExpect(jsonPath("$.errorMessage",
                                       containsString(String.format("Could not find employee events by the specified uuid '%s'", wrongUuid))))
                   .andExpect(jsonPath("$.httpStatus", is(404)));
        }
    }

    // ============================  End of class  ===========================
}
