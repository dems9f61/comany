package com.takeaway.employeeservice.department.control;

import com.takeaway.employeeservice.UnitTestSuite;
import com.takeaway.employeeservice.department.entity.Department;
import com.takeaway.employeeservice.runtime.errorhandling.entity.BadRequestException;
import com.takeaway.employeeservice.runtime.errorhandling.entity.ResourceNotFoundException;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.RandomUtils;
import org.assertj.core.util.Lists;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import java.util.List;

import static info.solidsoft.mockito.java8.AssertionMatcher.assertArg;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.verify;

/**
 * User: StMinko Date: 18.03.2019 Time: 14:33
 *
 * <p>
 */
@DisplayName("Unit tests for the department service")
class DepartmentServiceTest extends UnitTestSuite
{
    @Mock
    private DepartmentRepository departmentRepository;

    @InjectMocks
    private DepartmentService departmentService;

    @Nested
    @DisplayName("when new")
    class WhenNew
    {
        @Test
        @DisplayName("Creating a department with a empty department name fails")
        void givenEmptyDepartmentName_whenCreate_thenThrowException()
        {
            // Arrange
            DepartmentParameter creationParameter = departmentParameterTestFactory.builder()
                                                                                  .departmentName(" ")
                                                                                  .create();

            // Act / Assert
            assertThatExceptionOfType(BadRequestException.class).isThrownBy(() -> departmentService.create(creationParameter));
        }

        @Test
        @DisplayName("Creating a department with a null department name fails")
        void givenNullDepartmentName_whenCreate_thenThrowException()
        {
            // Arrange
            DepartmentParameter creationParameter = departmentParameterTestFactory.builder()
                                                                                  .departmentName(null)
                                                                                  .create();
            // Act / Assert
            assertThatExceptionOfType(BadRequestException.class).isThrownBy(() -> departmentService.create(creationParameter));
        }

        @Test
        @DisplayName("Creating a departments with a already existing name fails")
        void givenAlreadyExistingDepartmentName_whenCreate_thenThrowException()
        {
            // Arrange
            DepartmentParameter creationParameter = departmentParameterTestFactory.createDefault();
            doReturn(Lists.newArrayList(departmentTestFactory.createDefault())).when(departmentRepository)
                                                                               .findByDepartmentName(creationParameter.getDepartmentName());

            // Act / Assert
            assertThatExceptionOfType(BadRequestException.class).isThrownBy(() -> departmentService.create(creationParameter));
        }

        @Test
        @DisplayName("Creating a department with a valid parameter succeeds")
        void givenValidRequestParams_whenCreate_thenStatusSucceed() throws Exception
        {
            // Arrange
            DepartmentParameter creationParameter = departmentParameterTestFactory.createDefault();
            doReturn(Lists.emptyList()).when(departmentRepository)
                                       .findByDepartmentName(creationParameter.getDepartmentName());
            doReturn(departmentTestFactory.createDefault()).when(departmentRepository)
                                                           .save(any());

            // Act
            departmentService.create(creationParameter);

            // Assert
            verify(departmentRepository).findByDepartmentName(creationParameter.getDepartmentName());
            verify(departmentRepository).save(assertArg(departmentToSave -> assertThat(departmentToSave.getDepartmentName()).isEqualTo(
                    creationParameter.getDepartmentName())));
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
            List<Department> departments = departmentTestFactory.createManyDefault(RandomUtils.nextInt(10, 50));
            doReturn(departments).when(departmentRepository)
                                 .findAll();

            // Act
            List<Department> allDepartments = departmentService.findAll();

            // Assert
            assertThat(allDepartments).hasSameSizeAs(departments);
            verify(departmentRepository).findAll();
        }

        @Test
        @DisplayName("Finding a department with a wrong department name returns nothing")
        void givenNotExistingDepartmentName_whenFindByDepartmentName_thenThrowException() throws Exception
        {
            // Arrange
            String departmentName = RandomStringUtils.randomAlphabetic(24);
            doReturn(Lists.emptyList()).when(departmentRepository)
                                       .findByDepartmentName(departmentName);

            // Act / Assert
            assertThatExceptionOfType(ResourceNotFoundException.class).isThrownBy(() -> departmentService.findByDepartmentName(departmentName));
        }

        @Test
        @DisplayName("Finding a department with a correct department name returns the related department")
        void givenDepartments_whenFindByDepartmentName_thenReturnDepartment() throws Exception
        {
            // Arrange
            Department department = departmentTestFactory.createDefault();
            String departmentName = department.getDepartmentName();
            doReturn(Lists.newArrayList(department)).when(departmentRepository)
                                                    .findByDepartmentName(departmentName);

            // Act
            Department found = departmentService.findByDepartmentName(departmentName);

            // Assert
            verify(departmentRepository).findByDepartmentName(departmentName);
            assertThat(found).isEqualTo(department);
        }
    }
}
