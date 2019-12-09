package com.takeaway.employeeservice.department.boundary;

import com.takeaway.employeeservice.UnitTestSuite;
import com.takeaway.employeeservice.department.control.DepartmentServiceCapable;
import com.takeaway.employeeservice.department.entity.Department;
import com.takeaway.employeeservice.department.entity.DepartmentRequest;
import com.takeaway.employeeservice.department.entity.DepartmentResponse;
import com.takeaway.employeeservice.runtime.errorhandling.entity.BadRequestException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatExceptionOfType;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;

/**
 * User: StMinko Date: 18.03.2019 Time: 21:58
 *
 * <p>
 */
@DisplayName("Unit tests for the department controller")
class DepartmentControllerTest extends UnitTestSuite
{
    @Mock
    private DepartmentServiceCapable departmentService;

    @InjectMocks
    private DepartmentController departmentController;

    @Nested
    @DisplayName("when create")
    class WhenCreate
    {
        @Test
        @DisplayName("Creating a department throws BadRequestException when departmentService throws Exception")
        void givenUnderlyingException_whenCreate_thenThrowBadRequestException()
        {
            // Arrange
            DepartmentRequest departmentRequest = departmentRequestTestFactory.createDefault();
            doThrow(BadRequestException.class).when(departmentService)
                                              .create(departmentRequest.toDepartmentParameter());

            // Act / Assert
            assertThatExceptionOfType(BadRequestException.class).isThrownBy(() -> departmentController.create(departmentRequest));
        }

        @Test
        @DisplayName("Creating a department succeeds when request is valid")
        void giveValidRequest_whenCreate_thenSucceed()
        {
            // Arrange
            DepartmentRequest departmentRequest = departmentRequestTestFactory.createDefault();
            Department department = departmentTestFactory.createDefault();
            doReturn(department).when(departmentService)
                                .create(departmentRequest.toDepartmentParameter());

            // Act
            DepartmentResponse departmentResponse = departmentController.create(departmentRequest);

            // Assert
            assertThat(departmentResponse).isNotNull();
            assertThat(departmentResponse.getId()).isEqualTo(department.getId());
            assertThat(departmentResponse.getDepartmentName()).isEqualTo(department.getDepartmentName());
        }
    }
}
