package com.takeaway.employeeservice.department.control;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * User: StMinko Date: 18.03.2019 Time: 11:51
 *
 * <p>
 */
@Slf4j
@RequiredArgsConstructor
@Service
class DepartmentService implements DepartmentServiceCapable
{
  // =========================== Class Variables ===========================
  // =============================  Variables  =============================

  @Getter
  private final DepartmentRepository repository;

  // ============================  Constructors  ===========================
  // ===========================  public  Methods  =========================
  // =================  protected/package local  Methods ===================
  // ===========================  private  Methods  ========================
  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}
