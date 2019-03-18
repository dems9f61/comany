package com.takeaway.employeeservice.resttest;

import com.takeaway.employeeservice.ApiVersions;
import com.takeaway.employeeservice.department.api.dto.CreateDepartmentRequest;
import com.takeaway.employeeservice.department.api.dto.DepartmentResponse;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * User: StMinko
 * Date: 18.03.2019
 * Time: 22:50
 * <p/>
 */
@DisplayName("Rest tests for the department service")
class DepartmentRestTest extends RestTestSuite
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================
    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================
    // =================  protected/package local  Methods ===================

    @Nested
    @DisplayName("when create")
    class WhenCreate
    {
        @Test
        @DisplayName("POST: 'http://.../departments' returns BAD_REQUEST if the specified name is already used")
        void givenAlreadyUsedName_whenCreateUser_thenStatus400()
        {
            // Arrange
            String departmentName = RandomStringUtils.randomAlphabetic(23);
            CreateDepartmentRequest createDepartmentRequest = createDepartmentRequestTestFactory.builder()
                                                                                                .departmentName(departmentName)
                                                                                                .create();

            String uri = String.format("%s/departments", ApiVersions.V1);
            HttpHeaders headers = defaultHttpHeaders();
            testRestTemplate.exchange(uri, HttpMethod.POST, new HttpEntity<>(createDepartmentRequest, headers), DepartmentResponse.class);

            // Act
            ResponseEntity<Void> responseEntity = testRestTemplate.exchange(uri,
                                                                            HttpMethod.POST,
                                                                            new HttpEntity<>(createDepartmentRequest, headers),
                                                                            Void.class);
            // Assert
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("POST: 'http://.../departments' returns BAD_REQUEST if the specified name is blank")
        void givenBlankDepartmentName_whenCreateUser_thenStatus400()
        {
            // Arrange
            CreateDepartmentRequest createDepartmentRequest = createDepartmentRequestTestFactory.builder()
                                                                                                .departmentName("  ")
                                                                                                .create();

            String uri = String.format("%s/departments", ApiVersions.V1);
            HttpHeaders headers = defaultHttpHeaders();

            // Act
            ResponseEntity<Void> responseEntity = testRestTemplate.exchange(uri,
                                                                            HttpMethod.POST,
                                                                            new HttpEntity<>(createDepartmentRequest, headers),
                                                                            Void.class);
            // Assert
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("POST: 'http://.../departments' returns BAD_REQUEST if the specified name is null")
        void givenNullDepartmentName_whenCreateUser_thenStatus400()
        {
            // Arrange
            CreateDepartmentRequest createDepartmentRequest = createDepartmentRequestTestFactory.builder()
                                                                                                .departmentName(null)
                                                                                                .create();

            String uri = String.format("%s/departments", ApiVersions.V1);
            HttpHeaders headers = defaultHttpHeaders();

            // Act
            ResponseEntity<Void> responseEntity = testRestTemplate.exchange(uri,
                                                                            HttpMethod.POST,
                                                                            new HttpEntity<>(createDepartmentRequest, headers),
                                                                            Void.class);
            // Assert
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("POST: 'http://.../departments' returns CREATED if the specified name is valid")
        void givenValidDepartmentName_whenCreateUser_thenStatus200()
        {
            // Arrange
            CreateDepartmentRequest createDepartmentRequest = createDepartmentRequestTestFactory.createDefault();
            String uri = String.format("%s/departments", ApiVersions.V1);
            HttpHeaders headers = defaultHttpHeaders();

            // Act
            ResponseEntity<DepartmentResponse> responseEntity = testRestTemplate.exchange(uri,
                                                                                          HttpMethod.POST,
                                                                                          new HttpEntity<>(createDepartmentRequest, headers),
                                                                                          DepartmentResponse.class);

            // Assert
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            DepartmentResponse departmentResponse = responseEntity.getBody();
            assertThat(departmentResponse).isNotNull();
            assertThat(departmentResponse.getId()).isGreaterThan(0);
            assertThat(departmentResponse.getDepartmentName()).isEqualTo(createDepartmentRequest.getDepartmentName());
        }
    }

    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
