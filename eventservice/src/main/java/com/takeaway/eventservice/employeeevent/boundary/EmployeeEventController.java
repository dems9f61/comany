package com.takeaway.eventservice.employeeevent.boundary;

import com.takeaway.eventservice.ApiVersions;
import com.takeaway.eventservice.employeeevent.control.EmployeeEventService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.stream.Collectors;

/**
 * User: StMinko
 * Date: 20.03.2019
 * Time: 20:08
 * <p/>
 */
@Slf4j
@RestController
@Api(value = "Employee event service: Operations related to employee event service interface")
@RequestMapping(ApiVersions.V1 + "/events")
@RequiredArgsConstructor
public class EmployeeEventController
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================

    private final EmployeeEventService employeeEventService;

    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================

    @ApiOperation(value = "Retrieves all events related to an employee in ascending order")
    @GetMapping("/{uuid}")
    @ResponseStatus(HttpStatus.OK)
    public List<EmployeeEventResponse> getAllEmployeeEvents(@NotBlank @PathVariable("uuid") String uuid)
    {
        return employeeEventService.findAllByOrderByCreatedAtAsc(uuid)
                                   .stream()
                                   .map(EmployeeEventResponse::new)
                                   .collect(Collectors.toList());
    }

    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
