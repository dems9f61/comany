package com.takeaway.eventservice.employeeevent.control;

import com.takeaway.eventservice.employeeevent.entity.PersistentEmployeeEvent;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * User: StMinko
 * Date: 20.03.2019
 * Time: 18:41
 * <p/>
 */
@Repository
interface EmployeeEventRepository extends MongoRepository<PersistentEmployeeEvent, String>
{
    // =========================== Class Variables ===========================
    // ==============================  Methods  ==============================

    List<PersistentEmployeeEvent> findAllByOrderByCreatedAtAsc();

    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
