package com.takeaway.eventservice.employee_event.service;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User: StMinko
 * Date: 20.03.2019
 * Time: 18:41
 * <p/>
 */
@Repository
interface EmployeeEventRepository extends JpaRepository<PersistentEmployeeEvent, Integer>
{
    // =========================== Class Variables ===========================
    // ==============================  Methods  ==============================

    List<PersistentEmployeeEvent> findAllByOrderByIdAsc();

    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
