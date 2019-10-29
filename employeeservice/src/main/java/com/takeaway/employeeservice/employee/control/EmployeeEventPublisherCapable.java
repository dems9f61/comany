package com.takeaway.employeeservice.employee.control;

import com.takeaway.employeeservice.employee.entity.Employee;

/**
 * User: StMinko Date: 20.03.2019 Time: 16:22
 *
 * <p>
 */
public interface EmployeeEventPublisherCapable
{
  // =========================== Class Variables ===========================
  // ==============================  Methods  ==============================

  void employeeCreated(Employee createdEmployee);

  void employeeDeleted(Employee deletedEmployee);

  void employeeUpdated(Employee updatedEmployee);

  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}
