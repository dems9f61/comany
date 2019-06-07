package com.takeaway.employeeservice.aop;

import org.aspectj.lang.annotation.Pointcut;

/**
 * User: StMinko
 * Date: 07.06.2019
 * Time: 11:55
 * <p/>
 */
final class DatabaseEntryJoinPointConfig
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================
    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================

    @Pointcut("execution(* com.takeaway.employeeservice.employee.control.*.save(..))")
    public void saveEmployeeExecution()
    {
        //Since it's only purpose is to serve to pointcut, there is no need for any implementation here
    }

    @Pointcut("execution(* com.takeaway.employeeservice.department.control.*.save(..))")
    public void saveDepartmentExecution()
    {
        //Since it's only purpose is to serve to pointcut, there is no need for any implementation here
    }

    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
