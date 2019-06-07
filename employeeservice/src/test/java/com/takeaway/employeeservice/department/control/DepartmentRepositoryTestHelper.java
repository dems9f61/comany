package com.takeaway.employeeservice.department.control;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Set;

/**
 * User: StMinko
 * Date: 07.06.2019
 * Time: 13:09
 * <p/>
 */
@Component
@RequiredArgsConstructor
@Slf4j
@Transactional
public class DepartmentRepositoryTestHelper
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================

    private final DepartmentRepository departmentRepository;

    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================

    public void cleanDatabase(@NonNull Set<Long> idsToDelete){
        idsToDelete.forEach(idToDelete -> {
            LOGGER.info("Removing test department '{}'", idToDelete);
            departmentRepository.findById(idToDelete).ifPresent(departmentRepository::delete);
        });
    }

    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
