package com.takeaway.employeeservice.department.api;

import com.takeaway.employeeservice.ApiVersions;
import com.takeaway.employeeservice.common_api_exception.BadRequestException;
import com.takeaway.employeeservice.department.api.dto.CreateDepartmentRequest;
import com.takeaway.employeeservice.department.api.dto.DepartmentResponse;
import com.takeaway.employeeservice.department.service.CreateDepartmentParameter;
import com.takeaway.employeeservice.department.service.Department;
import com.takeaway.employeeservice.department.service.DepartmentServiceCapable;
import com.takeaway.employeeservice.department.service.DepartmentServiceException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;

/**
 * User: StMinko
 * Date: 18.03.2019
 * Time: 17:23
 * <p/>
 */
@Slf4j
@RestController
@RequestMapping(ApiVersions.V1 + "/departments")
@RequiredArgsConstructor
public class DepartmentController
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================

    private final DepartmentServiceCapable departmentService;

    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public DepartmentResponse createDepartment(@RequestBody @NotNull @Valid CreateDepartmentRequest createDepartmentRequest)
    {
        CreateDepartmentParameter createDepartmentParameter = createDepartmentRequest.toCreateDepartmentParameter();
        try
        {
            Department department = departmentService.create(createDepartmentParameter);
            return new DepartmentResponse(department.getId(), department.getDepartmentName());
        }
        catch (DepartmentServiceException caught)
        {
            throw new BadRequestException(caught.getMessage(), caught.getCause());
        }
    }

    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
