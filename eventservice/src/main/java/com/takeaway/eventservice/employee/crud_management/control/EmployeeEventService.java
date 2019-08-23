package com.takeaway.eventservice.employee.crud_management.control;

import com.takeaway.eventservice.employee.crud_management.entity.PersistentEmployeeEvent;
import com.takeaway.eventservice.employee.messaging.entity.Employee;
import com.takeaway.eventservice.employee.messaging.entity.EmployeeEvent;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.UUID;

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

    static final Sort CREATED_AT_WITH_ASC_SORT = new Sort(Sort.Direction.ASC, "createdAt");

    static final int MAX_PAGE_SIZE = 1000;

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
        persistentEmployeeEvent.setUuid(employee.getId());
        persistentEmployeeEvent.setDepartmentName(employee.getDepartment()
                                                          .getDepartmentName());
        employeeEventRepository.save(persistentEmployeeEvent);
    }

    public Page<PersistentEmployeeEvent> findByUuidOrderByCreatedAtAsc(@NonNull UUID uuid, Pageable pageable)
    {
        PageRequest createdAtPageRequest = PageRequest.of(pageable.getPageNumber(), MAX_PAGE_SIZE, CREATED_AT_WITH_ASC_SORT);
        return employeeEventRepository.findByUuid(uuid, createdAtPageRequest);
    }

    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
