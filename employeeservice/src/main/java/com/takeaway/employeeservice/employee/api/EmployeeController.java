package com.takeaway.employeeservice.employee.api;

import com.takeaway.employeeservice.ApiVersions;
import com.takeaway.employeeservice.common_api_exception.BadRequestException;
import com.takeaway.employeeservice.common_api_exception.ResourceNotFoundException;
import com.takeaway.employeeservice.employee.api.dto.EmployeeRequest;
import com.takeaway.employeeservice.employee.api.dto.EmployeeResponse;
import com.takeaway.employeeservice.employee.service.Employee;
import com.takeaway.employeeservice.employee.service.EmployeeParameter;
import com.takeaway.employeeservice.employee.service.EmployeeServiceCapable;
import com.takeaway.employeeservice.employee.service.EmployeeServiceException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.net.HttpURLConnection;

/**
 * User: StMinko
 * Date: 19.03.2019
 * Time: 23:18
 * <p/>
 */
@Slf4j
@RestController
@Api(value = "Employee service: Operations pertaining to the employee service interface")
@RequestMapping(ApiVersions.V1 + "/employees")
@RequiredArgsConstructor
public class EmployeeController
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================

    private final EmployeeServiceCapable employeeService;

    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================

    @ApiOperation(value = "Creates an employee with the request values")
    @ApiResponses({ @ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST, message = "") })
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public EmployeeResponse createEmployee(@RequestBody @NotNull @Valid EmployeeRequest employeeRequest)
    {
        LOGGER.info("Creating an employee by the id {}", employeeRequest);
        EmployeeParameter employeeParameter = employeeRequest.toEmployerParameter();
        try
        {
            Employee employee = employeeService.create(employeeParameter);
            return new EmployeeResponse(employee.getUuid(),
                                        employee.getEmailAddress(),
                                        employee.getFullName()
                                                .getFirstName(),
                                        employee.getFullName()
                                                .getLastName(),
                                        employee.getBirthday(),
                                        employee.getDepartment()
                                                .getDepartmentName());
        }
        catch (EmployeeServiceException caught)
        {
            throw new BadRequestException(caught.getMessage(), caught.getCause());
        }
    }

    @ApiOperation(value = "Retrieves an employee by a given uuid")
    @GetMapping("/{uuid}")
    @ApiResponses({
            @ApiResponse(code = HttpURLConnection.HTTP_NOT_FOUND, message = "Could not find employee by the specified uuid!") })
    @ResponseStatus(HttpStatus.OK)
    public EmployeeResponse findUserByUuid(@NotBlank @PathVariable("uuid") String uuid)
    {
        LOGGER.info("Retrieving an employee by the id {}", uuid);
        return employeeService.findByUuid(uuid)
                              .map(employee -> new EmployeeResponse(employee.getUuid(),
                                                                    employee.getEmailAddress(),
                                                                    employee.getFullName()
                                                                            .getFirstName(),
                                                                    employee.getFullName()
                                                                            .getLastName(),
                                                                    employee.getBirthday(),
                                                                    employee.getDepartment()
                                                                            .getDepartmentName()))
                              .orElseThrow(() -> new ResourceNotFoundException("Could not find employee by the specified uuid!"));
    }

    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
