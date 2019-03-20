package com.takeaway.employeeservice.employee.service;

/**
 * User: StMinko
 * Date: 20.03.2019
 * Time: 16:22
 * <p/>
 */
public interface EmployeeEventPublisherCapable
{
    // =========================== Class Variables ===========================
    // ==============================  Methods  ==============================

    void employeeCreated(Employee createdEmployee);

    void employeeDeleted(Employee deleteEmployee);

    void employeeUpdated(Employee updatedEmployee);

    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
