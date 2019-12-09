package com.takeaway.employeeservice.department.control;

import com.takeaway.employeeservice.IntegrationTestSuite;
import com.takeaway.employeeservice.department.entity.Department;
import com.takeaway.employeeservice.runtime.errorhandling.entity.BadRequestException;
import com.takeaway.employeeservice.runtime.errorhandling.entity.ResourceNotFoundException;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;

/**
 * User: StMinko Date: 18.03.2019 Time: 12:58
 *
 * <p>
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
        void givenValidRequestParams_whenCreate_thenStatusSucceed()
        {
            // Arrange
            DepartmentParameter creationParameter = departmentParameterTestFactory.createDefault();

            // Act
            Department department = departmentService.create(creationParameter);

            // Assert
            assertThat(department).isNotNull();
            assertThat(department.getId()).isGreaterThan(0L);
            assertThat(department.getDepartmentName()).isNotBlank().isEqualTo(creationParameter.getDepartmentName());
        }

        @Test
        @DisplayName("Creating a departments with a already existing name fails")
        void givenAlreadyExistingDepartmentName_whenCreate_thenThrowException()
        {
            // Arrange
            String departmentName = RandomStringUtils.randomAlphabetic(23);
            DepartmentParameter creationParameter = departmentParameterTestFactory.builder().departmentName(departmentName).create();
            departmentService.create(creationParameter);

            DepartmentParameter creationParameter_2 = departmentParameterTestFactory.builder().departmentName(departmentName).create();
            // Act / Assert
            assertThatExceptionOfType(BadRequestException.class).isThrownBy(() -> departmentService.create(creationParameter_2));
        }

        @Test
        @DisplayName("Creating a departments with a empty name fails")
        void givenBlankDepartmentName_whenCreate_thenThrowException()
        {
            // Arrange
            DepartmentParameter creationParameter = departmentParameterTestFactory.builder().departmentName(" ").create();
            // Act / Assert
            assertThatExceptionOfType(BadRequestException.class).isThrownBy(() -> departmentService.create(creationParameter));
        }

        @Test
        @DisplayName("Creating a departments with a null name fails")
        void givenNullDepartmentName_whenCreate_thenThrowException()
        {
            // Arrange
            DepartmentParameter creationParameter = departmentParameterTestFactory.builder().departmentName(null).create();
            // Act / Assert
            assertThatExceptionOfType(BadRequestException.class).isThrownBy(() -> departmentService.create(creationParameter));
        }
    }

    @Nested
    @DisplayName("when access")
    class WhenAccess
    {
        @Test
        @DisplayName("Finding all departments returns all existing departments")
        void givenDepartments_whenFindAll_thenReturnAll()
        {
            // Arrange
            departmentRepository.deleteAll();
            List<DepartmentParameter> creationParameters = departmentParameterTestFactory.createManyDefault(RandomUtils.nextInt(10, 50));
            for (DepartmentParameter creationParameter : creationParameters)
            {
                departmentService.create(creationParameter);
            }

            // Act
            List<Department> all = departmentService.findAll();

            // Assert
            assertThat(all.size()).isEqualTo(creationParameters.size());
        }

        @Test
        @DisplayName("Finding a department with a correct department name returns the related department")
        void givenDepartments_whenFindByDepartmentName_thenReturnDepartment()
        {
            // Arrange
            List<DepartmentParameter> creationParameters = departmentParameterTestFactory.createManyDefault(RandomUtils.nextInt(10, 50));
            for (DepartmentParameter creationParameter : creationParameters)
            {
                departmentService.create(creationParameter);
            }

            DepartmentParameter creationParameter = departmentParameterTestFactory.createDefault();
            Department department = departmentService.create(creationParameter);

            // Act
            Department foundDepartment = departmentService.findByDepartmentName(creationParameter.getDepartmentName());

            // Assert
            assertThat(foundDepartment.getId()).isEqualTo(department.getId());
            assertThat(foundDepartment.getDepartmentName()).isEqualTo(department.getDepartmentName());
        }

        @Test
        @DisplayName("Finding a department with a wrong department name returns nothing")
        void givenNotExistingDepartmentName_whenFindByDepartmentName_thenReturnNothing()
        {
            // Arrange
            List<DepartmentParameter> creationParameters = departmentParameterTestFactory.createManyDefault(RandomUtils.nextInt(10, 50));
            for (DepartmentParameter creationParameter : creationParameters)
            {
                departmentService.create(creationParameter);
            }
            String wrongDepartmentName = "unknownName";

            // Act / Assert
            assertThatExceptionOfType(ResourceNotFoundException.class).isThrownBy(() -> departmentService.findByDepartmentName(wrongDepartmentName));
        }
    }
}
