package com.takeaway.eventservice.employeeevent.control;

import com.takeaway.eventservice.employeeevent.entity.PersistentEmployeeEvent;
import com.takeaway.eventservice.messaging.EmployeeEvent;
import com.takeaway.eventservice.messaging.dto.Employee;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

/**
 * User: StMinko
 * Date: 20.03.2019
 * Time: 18:02
 * <p/>
 */
@Slf4j
@RequiredArgsConstructor
@Service
public class EmployeeEventService
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================

    private final EmployeeEventRepository employeeEventRepository;

    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================

    @Async
    @EventListener
    public void handleEmployeeEvent(@NonNull EmployeeEvent employeeEvent)
    {
        PersistentEmployeeEvent persistentEmployeeEvent = new PersistentEmployeeEvent();
        Employee employee = employeeEvent.getEmployee();
        persistentEmployeeEvent.setEventType(employeeEvent.getEventType());
        persistentEmployeeEvent.setBirthday(employee.getBirthday());
        persistentEmployeeEvent.setEmailAddress(employee.getEmailAddress());
        Employee.FullName fullName = employee.getFullName();
        if (fullName != null)
        {
            persistentEmployeeEvent.setFirstName(fullName.getFirstName());
            persistentEmployeeEvent.setLastName(fullName.getLastName());
        }
        persistentEmployeeEvent.setUuid(employee.getUuid());
        persistentEmployeeEvent.setDepartmentName(employee.getDepartment()
                                                          .getDepartmentName());
        employeeEventRepository.save(persistentEmployeeEvent);
    }

    public List<PersistentEmployeeEvent> findAllByOrderByCreatedAtAsc(@NonNull String uuid)
    {
        return employeeEventRepository.findAllByOrderByCreatedAtAsc()
                                      .parallelStream()
                                      .filter(event -> StringUtils.equals(event.getUuid(), uuid))
                                      .collect(Collectors.toList());
    }

    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}