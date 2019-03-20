package com.takeaway.employeeservice.department.api;

import com.takeaway.employeeservice.UnitTestSuite;
import com.takeaway.employeeservice.common_api_exception.BadRequestException;
import com.takeaway.employeeservice.department.api.dto.DepartmentRequest;
import com.takeaway.employeeservice.department.api.dto.DepartmentResponse;
import com.takeaway.employeeservice.department.service.Department;
import com.takeaway.employeeservice.department.service.DepartmentServiceCapable;
import com.takeaway.employeeservice.department.service.DepartmentServiceException;
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
 * User: StMinko
 * Date: 18.03.2019
 * Time: 21:58
 * <p/>
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
        void givenUnderlyingException_whenCreate_thenThrowBadRequestException() throws Exception
        {
            // Arrange
            DepartmentRequest departmentRequest = createDepartmentRequestTestFactory.createDefault();
            doThrow(DepartmentServiceException.class).when(departmentService)
                                                     .create(departmentRequest.toDepartmentParameter());

            // Act / Assert
            assertThatExceptionOfType(BadRequestException.class).isThrownBy(() -> departmentController.createDepartment(departmentRequest));
        }

        @Test
        @DisplayName("Creating a department succeeds when request is valid")
        void giveValidRequest_whenCreate_thenSucceed() throws Exception
        {
            // Arrange
            DepartmentRequest departmentRequest = createDepartmentRequestTestFactory.createDefault();
            Department department = departmentTestFactory.createDefault();
            doReturn(department).when(departmentService)
                                .create(departmentRequest.toDepartmentParameter());

            // Act
            DepartmentResponse departmentResponse = departmentController.createDepartment(departmentRequest);

            // Assert
            assertThat(departmentResponse).isNotNull();
            assertThat(departmentResponse.getId()).isEqualTo(department.getId());
            assertThat(departmentResponse.getDepartmentName()).isEqualTo(department.getDepartmentName());
        }
    }
}