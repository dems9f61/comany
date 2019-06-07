package com.takeaway.employeeservice.employee.control;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * User: StMinko
 * Date: 07.06.2019
 * Time: 13:15
 * <p/>
 */
@Component
@RequiredArgsConstructor
@Transactional
@Slf4j
public class EmployeeRepositoryTestHelper
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================

    private final EmployeeRepository employeeRepository;

    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================

    public void cleanDatabase(@NonNull Set<String> idsToDelete)
    {
        idsToDelete.forEach(idToDelete -> {
            LOGGER.info("Removing test employee '{}'", idToDelete);
            employeeRepository.findByUuid(idToDelete).ifPresent(employeeRepository::delete);
        });
    }

    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
