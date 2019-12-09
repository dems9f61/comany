package com.takeaway.employeeservice.resttest;

import com.takeaway.employeeservice.department.boundary.DepartmentController;
import com.takeaway.employeeservice.department.entity.CreateDepartmentRequest;
import com.takeaway.employeeservice.department.entity.DepartmentResponse;
import com.takeaway.employeeservice.employee.boundary.EmployeeController;
import com.takeaway.employeeservice.employee.entity.CreateEmployeeRequest;
import com.takeaway.employeeservice.employee.entity.EmployeeResponse;
import com.takeaway.employeeservice.employee.entity.UpdateEmployeeRequest;
import com.takeaway.employeeservice.employee.entity.UsableDateFormat;
import org.apache.commons.lang3.RandomStringUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.http.*;

import java.time.LocalDate;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * User: StMinko Date: 20.03.2019 Time: 00:46
 *
 * <p>
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
        void givenValidParameters_whenCreateEmployee_thenStatus201()
        {
            // Arrange
            String departmentName = RandomStringUtils.randomAlphabetic(23);
            createAndPersistDepartment(departmentName);
            CreateEmployeeRequest createEmployeeRequest = createEmployeeRequestTestFactory.builder()
                                                                                          .departmentName(departmentName)
                                                                                          .create();
            HttpHeaders headers = defaultHttpHeaders();

            // Act
            ResponseEntity<EmployeeResponse> responseEntity = testRestTemplate.exchange(EmployeeController.BASE_URI,
                                                                                        HttpMethod.POST,
                                                                                        new HttpEntity<>(createEmployeeRequest, headers),
                                                                                        EmployeeResponse.class);

            // Assert
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            EmployeeResponse employeeResponse = responseEntity.getBody();
            assertThat(employeeResponse).isNotNull();
            assertThat(employeeResponse.getId()).isNotNull();
            assertThat(employeeResponse.getDepartmentName()).isEqualTo(createEmployeeRequest.getDepartmentName());
            assertThat(employeeResponse.getEmailAddress()).isEqualTo(createEmployeeRequest.getEmailAddress());
            assertThat(employeeResponse.getFirstName()).isEqualTo(createEmployeeRequest.getFirstName());
            assertThat(employeeResponse.getLastName()).isEqualTo(createEmployeeRequest.getLastName());
            assertThat(employeeResponse.getBirthday()).isEqualTo(createEmployeeRequest.getBirthday());
        }

        @Test
        @DisplayName("POST: 'http://.../employees' returns CREATED even if only the department name is specified")
        void givenOnlyDepartment_whenCreateEmployee_thenStatus201()
        {
            String departmentName = RandomStringUtils.randomAlphabetic(23);
            createAndPersistDepartment(departmentName);
            CreateEmployeeRequest employeeRequest = createEmployeeRequestTestFactory.builder()
                                                                                    .birthday(null)
                                                                                    .emailAddress(null)
                                                                                    .firstName(null)
                                                                                    .lastName(null)
                                                                                    .departmentName(departmentName)
                                                                                    .create();
            HttpHeaders headers = defaultHttpHeaders();

            // Act
            ResponseEntity<EmployeeResponse> responseEntity = testRestTemplate.exchange(EmployeeController.BASE_URI,
                                                                                        HttpMethod.POST,
                                                                                        new HttpEntity<>(employeeRequest, headers),
                                                                                        EmployeeResponse.class);

            // Assert
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.CREATED);
            EmployeeResponse employeeResponse = responseEntity.getBody();
            assertThat(employeeResponse).isNotNull();
            assertThat(employeeResponse.getId()).isNotNull();
            assertThat(employeeResponse.getDepartmentName()).isEqualTo(employeeRequest.getDepartmentName());
            assertThat(employeeResponse.getEmailAddress()).isEqualTo(employeeRequest.getEmailAddress());
            assertThat(employeeResponse.getFirstName()).isEqualTo(employeeRequest.getFirstName());
            assertThat(employeeResponse.getLastName()).isEqualTo(employeeRequest.getLastName());
            assertThat(employeeResponse.getBirthday()).isEqualTo(employeeRequest.getBirthday());
        }

        @Test
        @DisplayName("POST: 'http://.../employees' returns BAD REQUEST if the specified department name doesn't exist ")
        void givenUnknownDepartment_whenCreateEmployee_thenStatus404()
        {
            CreateEmployeeRequest employeeRequest = createEmployeeRequestTestFactory.createDefault();
            HttpHeaders headers = defaultHttpHeaders();

            // Act
            ResponseEntity<String> responseEntity = testRestTemplate.exchange(EmployeeController.BASE_URI,
                                                                              HttpMethod.POST,
                                                                              new HttpEntity<>(employeeRequest, headers),
                                                                              String.class);

            // Assert
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("POST: 'http://.../employees' returns BAD REQUEST if the specified email already exists ")
        void givenAlreadyUsedEmail_whenCreateEmployee_thenStatus400()
        {
            // Arrange
            String departmentName = RandomStringUtils.randomAlphabetic(23);
            createAndPersistDepartment(departmentName);
            CreateEmployeeRequest employeeRequest = createEmployeeRequestTestFactory.builder()
                                                                                    .departmentName(departmentName)
                                                                                    .create();
            createAndPersistEmployee(employeeRequest);
            HttpHeaders headers = defaultHttpHeaders();

            CreateEmployeeRequest newEmployeeRequest = createEmployeeRequestTestFactory.builder()
                                                                                       .emailAddress(employeeRequest.getEmailAddress())
                                                                                       .create();

            // Act
            ResponseEntity<String> responseEntity = testRestTemplate.exchange(EmployeeController.BASE_URI,
                                                                              HttpMethod.POST,
                                                                              new HttpEntity<>(newEmployeeRequest, headers),
                                                                              String.class);

            // Assert
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("POST: 'http://.../employees' returns BAD REQUEST if no field is set")
        void givenEmptyRequest_whenCreateEmployee_thenStatus400()
        {
            // Arrange
            CreateEmployeeRequest createEmployeeRequest = createEmployeeRequestTestFactory.builder()
                                                                                          .birthday(null)
                                                                                          .emailAddress(null)
                                                                                          .firstName(null)
                                                                                          .lastName(null)
                                                                                          .departmentName(null)
                                                                                          .create();
            HttpHeaders headers = defaultHttpHeaders();

            // Act
            ResponseEntity<String> responseEntity = testRestTemplate.exchange(EmployeeController.BASE_URI,
                                                                              HttpMethod.POST,
                                                                              new HttpEntity<>(createEmployeeRequest, headers),
                                                                              String.class);

            // Assert
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("POST: 'http://.../employees' returns BAD REQUEST if no the request is null")
        void givenNullRequest_whenCreateEmployee_thenStatus400()
        {
            // Arrange
            HttpHeaders headers = defaultHttpHeaders();

            // Act
            ResponseEntity<String> responseEntity = testRestTemplate.exchange(EmployeeController.BASE_URI,
                                                                              HttpMethod.POST,
                                                                              new HttpEntity<>(null, headers),
                                                                              String.class);

            // Assert
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }
    }

    @Nested
    @DisplayName("when access")
    class WhenAccess
    {
        @Test
        @DisplayName("GET: 'http://.../employees/{id}' returns NOT FOUND if the specified uuid doesn't exist ")
        void givenUnknownUuid_whenFindEmployeeByUuid_thenStatus404()
        {
            // Arrange
            UUID wrongUuid = UUID.randomUUID();

            // Act
            ResponseEntity<Void> responseEntity = testRestTemplate.exchange(String.format("%s/%s", EmployeeController.BASE_URI, wrongUuid),
                                                                            HttpMethod.GET,
                                                                            new HttpEntity<>(defaultHttpHeaders()),
                                                                            Void.class);

            // Assert
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("GET: 'http://.../employees/{id}' returns OK if the specified id exists ")
        void givenEmployee_whenFindEmployeeById_thenStatus200()
        {
            // Arrange
            String departmentName = RandomStringUtils.randomAlphabetic(23);
            createAndPersistDepartment(departmentName);
            CreateEmployeeRequest createEmployeeRequest = createEmployeeRequestTestFactory.builder()
                                                                                          .departmentName(departmentName)
                                                                                          .create();
            EmployeeResponse persistedEmployee = createAndPersistEmployee(createEmployeeRequest);
            UUID uuidToFind = persistedEmployee.getId();

            // Act
            ResponseEntity<EmployeeResponse> responseEntity = testRestTemplate.exchange(String.format("%s/%s",
                                                                                                      EmployeeController.BASE_URI,
                                                                                                      uuidToFind),
                                                                                        HttpMethod.GET,
                                                                                        new HttpEntity<>(defaultHttpHeaders()),
                                                                                        EmployeeResponse.class);

            // Assert
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.OK);
            assertThat(responseEntity.getBody()).isEqualTo(persistedEmployee);
        }
    }

    @Nested
    @DisplayName("when partial update")
    class WhenPartialUpdate
    {
        @Test
        @DisplayName("PATCH: 'http://.../employees/{id}' returns NOT FOUND if the specified employee doesn't exist ")
        void givenUnknownEmployee_whenUpdateEmployee_thenStatus404()
        {
            // Arrange
            String departmentName = RandomStringUtils.randomAlphabetic(23);
            createAndPersistDepartment(departmentName);
            UpdateEmployeeRequest updateEmployeeRequest = updateEmployeeRequestTestFactory.builder()
                                                                                          .departmentName(departmentName)
                                                                                          .create();
            HttpHeaders headers = defaultHttpHeaders();
            UUID wrongUuid = UUID.randomUUID();

            // Act
            ResponseEntity<Void> responseEntity = testRestTemplate.exchange(String.format("%s/%s", EmployeeController.BASE_URI, wrongUuid),
                                                                            HttpMethod.PATCH,
                                                                            new HttpEntity<>(updateEmployeeRequest, headers),
                                                                            Void.class);

            // Assert
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("PATCH: 'http://.../employees/{id}' returns BAD REQUEST if the specified department doesn't exist ")
        void givenUnknownDepartment_whenUpdateEmployee_thenStatus404()
        {
            // Arrange
            String departmentName = RandomStringUtils.randomAlphabetic(23);
            createAndPersistDepartment(departmentName);

            CreateEmployeeRequest employeeRequest = createEmployeeRequestTestFactory.builder()
                                                                                    .departmentName(departmentName)
                                                                                    .create();
            EmployeeResponse persistedEmployee = createAndPersistEmployee(employeeRequest);
            UUID uuidToUpdate = persistedEmployee.getId();
            String unknownDepartmentName = RandomStringUtils.randomAlphabetic(32);
            UpdateEmployeeRequest updateRequest = updateEmployeeRequestTestFactory.builder()
                                                                                  .departmentName(unknownDepartmentName)
                                                                                  .create();
            HttpHeaders headers = defaultHttpHeaders();

            // Act
            ResponseEntity<Void> responseEntity = testRestTemplate.exchange(String.format("%s/%s", EmployeeController.BASE_URI, uuidToUpdate),
                                                                            HttpMethod.PATCH,
                                                                            new HttpEntity<>(updateRequest, headers),
                                                                            Void.class);

            // Assert
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.BAD_REQUEST);
        }

        @Test
        @DisplayName("PATCH: 'http://.../employees/{id} returns NO CONTENT if the specified parameters are valid")
        void givenValidParameters_whenUpdateEmployee_thenStatus204()
        {
            // Arrange
            String firstDepartmentName = RandomStringUtils.randomAlphabetic(23);
            createAndPersistDepartment(firstDepartmentName);

            CreateEmployeeRequest employeeRequest = createEmployeeRequestTestFactory.builder()
                                                                                    .departmentName(firstDepartmentName)
                                                                                    .create();
            EmployeeResponse persistedEmployee = createAndPersistEmployee(employeeRequest);
            UUID uuidToUpdate = persistedEmployee.getId();

            String secondDepartmentName = RandomStringUtils.randomAlphabetic(23);
            createAndPersistDepartment(secondDepartmentName);

            UpdateEmployeeRequest updateRequest = updateEmployeeRequestTestFactory.builder()
                                                                                  .departmentName(secondDepartmentName)
                                                                                  .create();
            HttpHeaders headers = defaultHttpHeaders();

            // Act
            ResponseEntity<EmployeeResponse> responseEntity = testRestTemplate.exchange(String.format("%s/%s",
                                                                                                      EmployeeController.BASE_URI,
                                                                                                      uuidToUpdate),
                                                                                        HttpMethod.PATCH,
                                                                                        new HttpEntity<>(updateRequest, headers),
                                                                                        EmployeeResponse.class);
            // Assert
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
            assertThat(responseEntity.getBody()).isNull();

            EmployeeResponse updatedEmployee = findPersistedEmployee(uuidToUpdate);
            assertThat(updatedEmployee.getEmailAddress()).isEqualTo(updateRequest.getEmailAddress());
            assertThat(updatedEmployee.getFirstName()).isEqualTo(updateRequest.getFirstName());
            assertThat(updatedEmployee.getLastName()).isEqualTo(updateRequest.getLastName());
            assertThat(updatedEmployee.getBirthday()).isEqualTo(updateRequest.getBirthday());
            assertThat(updatedEmployee.getDepartmentName()).isEqualTo(updateRequest.getDepartmentName());
        }

        @Test
        @DisplayName("PATCH: 'http://.../employees/{id} returns NO CONTENT on only updating birthday")
        void givenNewBirthDay_whenUpdateEmployee_thenStatus204()
        {
            // Arrange
            String departmentName = RandomStringUtils.randomAlphabetic(23);
            createAndPersistDepartment(departmentName);

            CreateEmployeeRequest employeeRequest = createEmployeeRequestTestFactory.builder()
                                                                                    .departmentName(departmentName)
                                                                                    .create();
            EmployeeResponse persistedEmployee = createAndPersistEmployee(employeeRequest);
            UUID uuidToUpdate = persistedEmployee.getId();

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern(UsableDateFormat.DEFAULT.getDateFormat());
            LocalDate localDate = LocalDate.parse("1979-12-03", dateFormatter);
            ZonedDateTime newBirthDay = localDate.atStartOfDay(ZoneOffset.UTC);
            UpdateEmployeeRequest updateRequest = updateEmployeeRequestTestFactory.builder()
                                                                                  .departmentName(null)
                                                                                  .firstName(null)
                                                                                  .lastName(null)
                                                                                  .birthday(newBirthDay)
                                                                                  .emailAddress(null)
                                                                                  .create();
            HttpHeaders headers = defaultHttpHeaders();

            // Act
            ResponseEntity<EmployeeResponse> responseEntity = testRestTemplate.exchange(String.format("%s/%s",
                                                                                                      EmployeeController.BASE_URI,
                                                                                                      uuidToUpdate),
                                                                                        HttpMethod.PATCH,
                                                                                        new HttpEntity<>(updateRequest, headers),
                                                                                        EmployeeResponse.class);
            // Assert
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
            assertThat(responseEntity.getBody()).isNull();

            EmployeeResponse updatedEmployee = findPersistedEmployee(uuidToUpdate);
            assertThat(updatedEmployee.getEmailAddress()).isEqualTo(persistedEmployee.getEmailAddress());
            assertThat(updatedEmployee.getFirstName()).isEqualTo(persistedEmployee.getFirstName());
            assertThat(updatedEmployee.getLastName()).isEqualTo(persistedEmployee.getLastName());
            assertThat(updatedEmployee.getBirthday()).isEqualTo(newBirthDay);
            assertThat(updatedEmployee.getDepartmentName()).isEqualTo(persistedEmployee.getDepartmentName());
        }
    }

    @Nested
    @DisplayName("when delete")
    class WhenDelete
    {
        @Test
        @DisplayName("DELETE: 'http://.../employees/{id}' returns NOT FOUND if the specified uuid doesn't exist")
        void givenUnknownUuid_whenDeleteEmployeeByUuid_thenStatus404()
        {
            // Arrange
            UUID wrongUuid = UUID.randomUUID();

            // Act
            ResponseEntity<Void> responseEntity = testRestTemplate.exchange(String.format("%s/%s", EmployeeController.BASE_URI, wrongUuid),
                                                                            HttpMethod.DELETE,
                                                                            new HttpEntity<>(defaultHttpHeaders()),
                                                                            Void.class);

            // Assert
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        }

        @Test
        @DisplayName("DELETE: 'http://.../employees/{id}' returns NO CONTENT if the specified uuid exists")
        void givenEmployee_whenDeleteEmployeeByUuid_thenStatus204()
        {
            // Arrange
            String departmentName = RandomStringUtils.randomAlphabetic(23);
            createAndPersistDepartment(departmentName);
            CreateEmployeeRequest employeeRequest = createEmployeeRequestTestFactory.builder()
                                                                                    .departmentName(departmentName)
                                                                                    .create();
            EmployeeResponse persistedEmployee = createAndPersistEmployee(employeeRequest);
            UUID uuidToDelete = persistedEmployee.getId();

            // Act
            ResponseEntity<EmployeeResponse> responseEntity = testRestTemplate.exchange(String.format("%s/%s",
                                                                                                      EmployeeController.BASE_URI,
                                                                                                      uuidToDelete),
                                                                                        HttpMethod.DELETE,
                                                                                        new HttpEntity<>(defaultHttpHeaders()),
                                                                                        EmployeeResponse.class);

            // Assert
            assertThat(responseEntity.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
            assertThat(responseEntity.getBody()).isNull();
        }
    }

    private void createAndPersistDepartment(String departmentName)
    {
        CreateDepartmentRequest createDepartmentRequest = createDepartmentRequestTestFactory.builder()
                                                                                            .departmentName(departmentName)
                                                                                            .create();
        HttpHeaders headers = defaultHttpHeaders();
        testRestTemplate.exchange(DepartmentController.BASE_URI,
                                  HttpMethod.POST,
                                  new HttpEntity<>(createDepartmentRequest, headers),
                                  DepartmentResponse.class);
    }

    private EmployeeResponse createAndPersistEmployee(CreateEmployeeRequest employeeRequest)
    {
        // Arrange
        HttpHeaders headers = defaultHttpHeaders();

        // Act
        return testRestTemplate.exchange(EmployeeController.BASE_URI,
                                         HttpMethod.POST,
                                         new HttpEntity<>(employeeRequest, headers),
                                         EmployeeResponse.class)
                               .getBody();
    }

    private EmployeeResponse findPersistedEmployee(UUID idToFind)
    {
        ResponseEntity<EmployeeResponse> responseEntity = testRestTemplate.exchange(String.format("%s/%s", EmployeeController.BASE_URI, idToFind),
                                                                                    HttpMethod.GET,
                                                                                    new HttpEntity<>(defaultHttpHeaders()),
                                                                                    EmployeeResponse.class);
        return responseEntity.getBody();
    }
}
