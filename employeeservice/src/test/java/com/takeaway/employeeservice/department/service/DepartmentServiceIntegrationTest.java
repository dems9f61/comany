package com.takeaway.employeeservice.department.service;

import com.takeaway.employeeservice.IntegrationTestSuite;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * User: StMinko
 * Date: 18.03.2019
 * Time: 12:58
 * <p/>
 */
@DisplayName("Integration tests for the department service")
class DepartmentServiceIntegrationTest extends IntegrationTestSuite
{
    @Autowired
    private DepartmentRepository departmentRepository;

    @Autowired
    private DepartmentService departmentService;

    @Nested
    @DisplayName("when new")
    class WhenNew
    {
        @Test
        @DisplayName("Creating a department with a valid parameter succeeds")
        void givenValidRequestParams_whenCreate_thenStatusSucceed() throws Exception
        {
            // Arrange
            CreateDepartmentParameter creationParameter = createDepartmentParameterTestFactory.createDefault();

            // Act
            Department department = departmentService.create(creationParameter);

            // Assert
            assertThat(department).isNotNull();
            assertThat(department.getId()).isGreaterThan(0L);
            assertThat(department.getDepartmentName()).isNotBlank()
                                                      .isEqualTo(creationParameter.getDepartmentName());
        }

        @Test
        @DisplayName("Creating a departments with a already existing name fails")
        void givenAlreadyExistingDepartmentName_whenCreate_thenThrowException() throws Exception
        {
            // Arrange
            String departmentName = RandomStringUtils.randomAlphabetic(23);
            CreateDepartmentParameter creationParameter = createDepartmentParameterTestFactory.builder()
                                                                                              .departmentName(departmentName)
                                                                                              .create();
            departmentService.create(creationParameter);

            CreateDepartmentParameter creationParameter_2 = createDepartmentParameterTestFactory.builder()
                                                                                                .departmentName(departmentName)
                                                                                                .create();
            // Act / Assert
            assertThatExceptionOfType(DepartmentServiceException.class).isThrownBy(() -> departmentService.create(creationParameter_2));
        }

        @Test
        @DisplayName("Creating a departments with a empty name fails")
        void givenBlankDepartmentName_whenCreate_thenThrowException()
        {
            // Arrange
            CreateDepartmentParameter creationParameter = createDepartmentParameterTestFactory.builder()
                                                                                              .departmentName(" ")
                                                                                              .create();
            // Act / Assert
            assertThatExceptionOfType(DepartmentServiceException.class).isThrownBy(() -> departmentService.create(creationParameter));
        }

        @Test
        @DisplayName("Creating a departments with a null name fails")
        void givenNullDepartmentName_whenCreate_thenThrowException()
        {
            // Arrange
            CreateDepartmentParameter creationParameter = createDepartmentParameterTestFactory.builder()
                                                                                              .departmentName(null)
                                                                                              .create();
            // Act / Assert
            assertThatExceptionOfType(DepartmentServiceException.class).isThrownBy(() -> departmentService.create(creationParameter));
        }
    }

    @Nested
    @DisplayName("when access")
    class WhenAccess
    {
        @Test
        @DisplayName("Finding all departments returns all existing departments")
        void givenDepartments_whenFindAll_thenReturnAll() throws Exception
        {
            // Arrange
            departmentRepository.deleteAll();
            List<CreateDepartmentParameter> creationParameters = createDepartmentParameterTestFactory.createManyDefault(RandomUtils.nextInt(10, 50));
            for (CreateDepartmentParameter creationParameter : creationParameters)
            {
                departmentService.create(creationParameter);
            }

            // Act
            List<Department> all = departmentService.findAll();

            // Assert
            assertThat(all).hasSameSizeAs(creationParameters);
        }

        @Test
        @DisplayName("Finding a department with a correct department name returns the related department")
        void givenDepartments_whenFindByDepartmentName_thenReturnDepartment() throws Exception
        {
            // Arrange
            List<CreateDepartmentParameter> creationParameters = createDepartmentParameterTestFactory.createManyDefault(RandomUtils.nextInt(10, 50));
            for (CreateDepartmentParameter creationParameter : creationParameters)
            {
                departmentService.create(creationParameter);
            }

            CreateDepartmentParameter creationParameter = createDepartmentParameterTestFactory.createDefault();
            Department department = departmentService.create(creationParameter);

            // Act
            Optional<Department> found = departmentService.findByDepartmentName(creationParameter.getDepartmentName());

            // Assert
            assertThat(found).isPresent()
                             .hasValue(department);
        }

        @Test
        @DisplayName("Finding a department with a wrong department name returns nothing")
        void givenNotExistingDepartmentName_whenFindByDepartmentName_thenReturnNothing() throws Exception
        {
            // Arrange
            List<CreateDepartmentParameter> creationParameters = createDepartmentParameterTestFactory.createManyDefault(RandomUtils.nextInt(10, 50));
            for (CreateDepartmentParameter creationParameter : creationParameters)
            {
                departmentService.create(creationParameter);
            }
            String wrongDepartmentName = "-23";

            // Act
            Optional<Department> found = departmentService.findByDepartmentName(wrongDepartmentName);

            // Assert
            assertThat(found).isEmpty();
        }
    }
}
