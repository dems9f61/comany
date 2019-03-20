package com.takeaway.employeeservice.resttest;

import com.takeaway.employeeservice.ApiVersions;
import com.takeaway.employeeservice.department.api.dto.DepartmentRequest;
import com.takeaway.employeeservice.department.api.dto.DepartmentResponse;
import com.takeaway.employeeservice.employee.api.dto.EmployeeRequest;
import com.takeaway.employeeservice.employee.api.dto.EmployeeResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;

import java.time.ZoneId;
import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * User: StMinko
 * Date: 20.03.2019
 * Time: 00:46
 * <p/>
 */
@DisplayName("Rest tests for the employee service")
class EmployeeRestTest extends RestTestSuite
{
    @Nested
    @DisplayName("when create")
    class WhenCreate
    {
        @Test
        @DisplayName("POST: 'http://.../employees' returns CREATED if the specified parameters are valid")
        void givenValidParameters_whenCreateUser_thenStatus201()
        {
            // Arrange
            String departmentName = RandomStringUtils.randomAlphabetic(23);
            createAndPersistDepartment(departmentName);
            EmployeeRequest employeeRequest = employeeRequestTestFactory.builder()
                                                                        .departmentName(departmentName)
                                                                        .create();
            String uri = String.format("%s/employees", ApiVersions.V1);
            HttpHeaders headers = defaultHttpHeaders();

            // Act
            ResponseEntity<EmployeeResponse> responseEntity = testRestTemplate.exchange(uri,
                                                                                        HttpMethod.POST,
                                                                                        new HttpEntity<>(employeeRequest, headers),
                                                                                        EmployeeResponse.class);

            // Assert
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            EmployeeResponse employeeResponse = responseEntity.getBody();
            assertThat(employeeResponse).isNotNull();
            assertThat(employeeResponse.getUuid()).isNotBlank();
            assertThat(employeeResponse.getDepartmentName()).isEqualTo(employeeRequest.getDepartmentName());
            assertThat(employeeResponse.getEmailAddress()).isEqualTo(employeeRequest.getEmailAddress());
            assertThat(employeeResponse.getFirstName()).isEqualTo(employeeRequest.getFirstName());
            assertThat(employeeResponse.getLastName()).isEqualTo(employeeRequest.getLastName());
            assertThat(employeeResponse.getBirthday()).isEqualTo(Date.from(employeeRequest.getBirthday()
                                                                                          .atStartOfDay(ZoneId.systemDefault())
                                                                                          .toInstant()));
        }

        @Test
        @DisplayName("POST: 'http://.../employees' returns BAD REQUEST if the specified department name doesn't exist ")
        void givenUnknownDepartment_whenCreateUser_thenStatus400()
        {
            EmployeeRequest employeeRequest = employeeRequestTestFactory.createDefault();
            String uri = String.format("%s/employees", ApiVersions.V1);
            HttpHeaders headers = defaultHttpHeaders();

            // Act
            ResponseEntity<String> responseEntity = testRestTemplate.exchange(uri,
                                                                              HttpMethod.POST,
                                                                              new HttpEntity<>(employeeRequest, headers),
                                                                              String.class);

            // Assert
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("POST: 'http://.../employees' returns BAD REQUEST if the specified email already exists ")
        void givenAlreadyUsedEmail_whenCreateUser_thenStatus400()
        {
            // Arrange
            String departmentName = RandomStringUtils.randomAlphabetic(23);
            createAndPersistDepartment(departmentName);
            EmployeeRequest employeeRequest = employeeRequestTestFactory.builder()
                                                                        .departmentName(departmentName)
                                                                        .create();
            createAndPersistEmployee(employeeRequest);
            String uri = String.format("%s/employees", ApiVersions.V1);
            HttpHeaders headers = defaultHttpHeaders();

            EmployeeRequest newEmployeeRequest = employeeRequestTestFactory.builder()
                                                                           .emailAddress(employeeRequest.getEmailAddress())
                                                                           .create();

            // Act
            ResponseEntity<String> responseEntity = testRestTemplate.exchange(uri,
                                                                              HttpMethod.POST,
                                                                              new HttpEntity<>(newEmployeeRequest, headers),
                                                                              String.class);

            // Assert
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }

    private void createAndPersistDepartment(String departmentName)
    {
        DepartmentRequest createDepartmentRequest = departmentRequestTestFactory.builder()
                                                                                .departmentName(departmentName)
                                                                                .create();
        String uri = String.format("%s/departments", ApiVersions.V1);
        HttpHeaders headers = defaultHttpHeaders();
        testRestTemplate.exchange(uri, HttpMethod.POST, new HttpEntity<>(createDepartmentRequest, headers), DepartmentResponse.class);
    }

    private void createAndPersistEmployee(EmployeeRequest employeeRequest)
    {
        String uri = String.format("%s/employees", ApiVersions.V1);
        HttpHeaders headers = defaultHttpHeaders();

        // Act
        testRestTemplate.exchange(uri, HttpMethod.POST, new HttpEntity<>(employeeRequest, headers), EmployeeResponse.class);
    }
}
