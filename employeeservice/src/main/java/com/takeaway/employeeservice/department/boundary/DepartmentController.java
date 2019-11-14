package com.takeaway.employeeservice.department.boundary;

import com.takeaway.employeeservice.department.control.DepartmentParameter;
import com.takeaway.employeeservice.department.control.DepartmentServiceCapable;
import com.takeaway.employeeservice.department.control.DepartmentServiceException;
import com.takeaway.employeeservice.department.entity.Department;
import com.takeaway.employeeservice.department.entity.DepartmentRequest;
import com.takeaway.employeeservice.department.entity.DepartmentResponse;
import com.takeaway.employeeservice.errorhandling.entity.BadRequestException;
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

/**
 * User: StMinko Date: 18.03.2019 Time: 17:23
 *
 * <p>
 */
@Slf4j
@Validated
@RestController
@Api(value = "Department service: Operations pertaining to department service interface")
@RequestMapping(value = DepartmentController.BASE_URI,
    produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE},
    consumes = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class DepartmentController
{
  // =========================== Class Variables ===========================

  public static final String BASE_URI = ApiVersions.V1 + "/departments";

  // =============================  Variables  =============================

  private final DepartmentServiceCapable departmentService;

  // ============================  Constructors  ===========================
  // ===========================  public  Methods  =========================
  // =================  protected/package local  Methods ===================

  @ApiOperation(value = "Creates a department with the request values")
  @ApiResponses({@ApiResponse(code = HttpURLConnection.HTTP_BAD_REQUEST,message = "")})
  @PostMapping
  @ResponseStatus(HttpStatus.CREATED)
  DepartmentResponse createDepartment(@RequestBody @NotNull @Valid DepartmentRequest departmentRequest)
  {
    LOGGER.info("Creating a department by the request [{}]", departmentRequest);
    DepartmentParameter createDepartmentParameter = departmentRequest.toDepartmentParameter();
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

  // ===========================  private  Methods  ========================
  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}
