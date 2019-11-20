package com.takeaway.employeeservice.employee.boundary;

import com.takeaway.employeeservice.employee.control.EmployeeParameter;
import com.takeaway.employeeservice.employee.control.EmployeeServiceCapable;
import com.takeaway.employeeservice.employee.control.EmployeeServiceException;
import com.takeaway.employeeservice.employee.entity.CreateEmployeeRequest;
import com.takeaway.employeeservice.employee.entity.Employee;
import com.takeaway.employeeservice.employee.entity.EmployeeResponse;
import com.takeaway.employeeservice.employee.entity.UpdateEmployeeRequest;
import com.takeaway.employeeservice.errorhandling.entity.ApiException;
import com.takeaway.employeeservice.errorhandling.entity.BadRequestException;
import com.takeaway.employeeservice.errorhandling.entity.InternalServerErrorException;
import com.takeaway.employeeservice.errorhandling.entity.ResourceNotFoundException;
import com.takeaway.employeeservice.rest.boundary.ApiVersions;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.net.HttpURLConnection;
import java.util.UUID;

/**
 * User: StMinko Date: 19.03.2019 Time: 23:18
 *
 * <p>
 */
@Slf4j
@Validated
@RestController
@Api(value = "Employee service: Operations pertaining to the employee service interface")
@RequestMapping(value = EmployeeController.BASE_URI,
    produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE},
    consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class EmployeeController
{
  // =========================== Class Variables ===========================

  public static final String BASE_URI = ApiVersions.V1 + "/employees";

  // =============================  Variables  =============================

  private final EmployeeServiceCapable employeeService;

  // ============================  Constructors  ===========================
  // ===========================  public  Methods  =========================
  // =================  protected/package local  Methods ===================

  @ApiOperation(value = "Creates an employee with the request values")
  @ApiResponses({@ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST,message = ""), @ApiResponse(code = HttpURLConnection.HTTP_NOT_FOUND,message = "")})
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  EmployeeResponse createEmployee(@RequestBody @NotNull @Valid CreateEmployeeRequest createEmployeeRequest)
  {
    LOGGER.info("Creating an employee by the request [{}]", createEmployeeRequest);
    EmployeeParameter employeeParameter = createEmployeeRequest.toEmployerParameter();
    try
    {
      Employee employee = employeeService.create(employeeParameter);
      return new EmployeeResponse(employee.getId(),
          employee.getEmailAddress(),
          employee.getFullName().getFirstName(),
          employee.getFullName().getLastName(),
          employee.getBirthday(),
          employee.getDepartment().getDepartmentName());
    }
    catch (EmployeeServiceException caught)
    {
      throw translateInApiException(caught);
    }
  }

  @ApiOperation(value = "Retrieves an employee by a given uuid")
  @GetMapping("/{id}")
  @ApiResponses({@ApiResponse(code = HttpURLConnection.HTTP_NOT_FOUND,message = "Could not find employee by the specified uuid!")})
  @ResponseStatus(HttpStatus.OK)
  EmployeeResponse findEmployee(@NotNull @PathVariable("id") UUID uuid)
  {
    LOGGER.info("Retrieving an employee by the uuid [{}]", uuid);
    return employeeService
        .findById(uuid)
        .map(employee -> {
              Employee.FullName fullName = employee.getFullName();
              return new EmployeeResponse(employee.getId(),
                  employee.getEmailAddress(),
                  fullName != null ? fullName.getFirstName() : null,
                  fullName != null ? fullName.getLastName() : null,
                  employee.getBirthday(),
                  employee.getDepartment().getDepartmentName());
            })
        .orElseThrow(() -> new ResourceNotFoundException("Could not find employee by the specified uuid!"));
  }

  @ApiOperation(value = "Updates an employee with the request values")
  @ApiResponses({@ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST,message = ""), @ApiResponse(code = HttpURLConnection.HTTP_NOT_FOUND,message = "")})
  @PatchMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void updateEmployee(@NotNull @PathVariable("id") UUID uuid, @Valid @RequestBody UpdateEmployeeRequest updateEmployeeRequest)
  {
    LOGGER.info("Updating an employee by the uuid [{}] and request [{}]", uuid, updateEmployeeRequest);
    try
    {
      employeeService.update(uuid, updateEmployeeRequest.toEmployerParameter());
    }
    catch (EmployeeServiceException caught)
    {
      throw translateInApiException(caught);
    }
  }

  @ApiOperation(value = "Deletes permanently an employee")
  @ApiResponses({@ApiResponse(code = HttpURLConnection.HTTP_NOT_FOUND,message = "Could not delete employee by the specified uuid!")})
  @DeleteMapping("/{id}")
  @ResponseStatus(HttpStatus.NO_CONTENT)
  void deleteEmployee(@NotNull @PathVariable("id") UUID uuid)
  {
    LOGGER.info("Deleting an employee by the uuid [{}]", uuid);
    try
    {
      employeeService.deleteById(uuid);
    }
    catch (EmployeeServiceException caught)
    {
      EmployeeServiceException.Reason reason = caught.getReason();
      if (reason == EmployeeServiceException.Reason.NOT_FOUND)
      {
        throw new ResourceNotFoundException(caught.getMessage());
      }
      else
      {
        throw new InternalServerErrorException(caught.getMessage());
      }
    }
  }

  // ===========================  private  Methods  ========================

  private ApiException translateInApiException(EmployeeServiceException caught)
  {
    EmployeeServiceException.Reason reason = caught.getReason();
    switch (reason)
    {
      case NOT_FOUND:
        return new ResourceNotFoundException(caught.getMessage());
      case INVALID_REQUEST:
        return new BadRequestException(caught.getMessage(), caught.getCause());
      default:
        return new InternalServerErrorException(caught.getMessage());
    }
  }

  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}
