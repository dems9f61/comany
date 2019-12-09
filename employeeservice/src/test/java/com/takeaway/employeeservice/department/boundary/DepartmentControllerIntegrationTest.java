package com.takeaway.employeeservice.department.boundary;

import com.takeaway.employeeservice.IntegrationTestSuite;
import com.takeaway.employeeservice.department.entity.CreateDepartmentRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * User: StMinko Date: 14.05.2019 Time: 16:15
 *
 * <p>
 */
@AutoConfigureMockMvc
class DepartmentControllerIntegrationTest extends IntegrationTestSuite
{
    @Autowired
    private MockMvc mockMvc;

    @Nested
    @DisplayName("when new")
    class WhenAccess
    {
        @Test
        @DisplayName("POST: 'http://.../departments' returns BAD_REQUEST on empty department name")
        void givenEmptyDepartmentName_whenCreate_thenStatus400() throws Exception
        {
            // Arrange
            CreateDepartmentRequest createDepartmentRequest = createDepartmentRequestTestFactory.builder()
                                                                                                .departmentName("")
                                                                                                .create();

            // Act / Assert
            mockMvc.perform(post(DepartmentController.BASE_URI).contentType(MediaType.APPLICATION_JSON_UTF8)
                                                               .content(objectMapper.writeValueAsString(createDepartmentRequest)))
                   .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("POST: 'http://.../departments' returns BAD_REQUEST on blank department name")
        void givenBlankDepartmentName_whenCreate_thenStatus400() throws Exception
        {
            // Arrange
            CreateDepartmentRequest createDepartmentRequest = createDepartmentRequestTestFactory.builder()
                                                                                                .departmentName("  ")
                                                                                                .create();

            // Act / Assert
            mockMvc.perform(post(DepartmentController.BASE_URI).contentType(MediaType.APPLICATION_JSON_UTF8)
                                                               .content(objectMapper.writeValueAsString(createDepartmentRequest)))
                   .andExpect(status().isBadRequest());
        }

        @Test
        @DisplayName("POST: 'http://.../departments' returns BAD_REQUEST on null department name")
        void givenNullDepartmentName_whenCreate_thenStatus400() throws Exception
        {
            // Arrange
            CreateDepartmentRequest createDepartmentRequest = createDepartmentRequestTestFactory.builder()
                                                                                                .departmentName("")
                                                                                                .create();

            // Act / Assert
            mockMvc.perform(post(DepartmentController.BASE_URI).contentType(MediaType.APPLICATION_JSON_UTF8)
                                                               .content(objectMapper.writeValueAsString(createDepartmentRequest)))
                   .andExpect(status().isBadRequest());
        }
    }
}
