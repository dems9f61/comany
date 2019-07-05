package com.takeaway.eventservice.employeeevent.management.control;

import com.takeaway.eventservice.employeeevent.management.entity.PersistentEmployeeEvent;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

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

    Page<PersistentEmployeeEvent> findByUuid(String uuid, Pageable pageable);

    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
