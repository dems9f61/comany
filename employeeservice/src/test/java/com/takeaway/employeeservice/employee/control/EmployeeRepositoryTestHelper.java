package com.takeaway.employeeservice.employee.control;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * User: StMinko Date: 07.06.2019 Time: 13:15
 *
 * <p>
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

  public void cleanDatabase()
  {
    LOGGER.info("Cleaning the employee repository");
    employeeRepository.deleteAll();
  }

  // =================  protected/package local  Methods ===================
  // ===========================  private  Methods  ========================
  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}
