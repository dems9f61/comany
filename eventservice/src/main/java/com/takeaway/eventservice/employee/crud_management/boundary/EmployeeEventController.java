package com.takeaway.eventservice.employee.crud_management.boundary;

import com.takeaway.eventservice.ApiVersions;
import com.takeaway.eventservice.employee.crud_management.control.EmployeeEventService;
import com.takeaway.eventservice.employee.crud_management.entity.ApiResponsePage;
import com.takeaway.eventservice.employee.crud_management.entity.EmployeeEventResponse;
import com.takeaway.eventservice.integrationsupport.entity.ResourceNotFoundException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotNull;
import java.util.UUID;

/**
 * User: StMinko Date: 20.03.2019 Time: 20:08
 *
 * <p>
 */
@Slf4j
@Validated
@RestController
@Api(value = "Employee event service: Operations related to employee event service interface")
@RequestMapping(value = EmployeeEventController.BASE_URI,
    produces = {MediaType.APPLICATION_JSON_UTF8_VALUE, MediaType.APPLICATION_JSON_VALUE})
@RequiredArgsConstructor
public class EmployeeEventController
{
  // =========================== Class Variables ===========================

  public static final String BASE_URI = ApiVersions.V1 + "/events";

  // =============================  Variables  =============================

  private final EmployeeEventService employeeEventService;

  // ============================  Constructors  ===========================
  // ===========================  public  Methods  =========================

  @ApiOperation(value = "Retrieves all events related to an employee in ascending order")
  @GetMapping("/{uuid}")
  @ResponseStatus(HttpStatus.OK)
  ApiResponsePage<EmployeeEventResponse> findByUuidOrderByCreatedAtAsc(@NotNull @PathVariable("uuid") UUID uuid, Pageable pageable)
  {
    Page<EmployeeEventResponse> employeeEventResponses = employeeEventService.findByUuidOrderByCreatedAtAsc(uuid, pageable).map(EmployeeEventResponse::new);
    if (employeeEventResponses.isEmpty())
    {
      throw new ResourceNotFoundException(String.format("Could not find employee events by the specified uuid [%s]", uuid));
    }
    else
    {
      return new ApiResponsePage<>(employeeEventResponses.getContent(), pageable, employeeEventResponses.getTotalElements());
    }
  }

  // =================  protected/package local  Methods ===================
  // ===========================  private  Methods  ========================
  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}
