package com.takeaway.eventservice.employee_event.service;

import com.takeaway.eventservice.messaging.EmployeeEvent;
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
        PersistentEmployeeEvent persistentEmployeeEvent = employeeEvent.toPersistent();
        employeeEventRepository.save(persistentEmployeeEvent);
    }

    public List<PersistentEmployeeEvent> findAllByOrderByIdAsc(String uuid)
    {
        return employeeEventRepository.findAllByOrderByIdAsc()
                                      .parallelStream()
                                      .filter(event -> StringUtils.equals(event.getUuid(), uuid))
                                      .collect(Collectors.toList());
    }

    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
