package com.takeaway.eventservice.employee.crud_management.control;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * User: StMinko Date: 15.10.2019 Time: 15:48
 *
 * <p>
 */
@Component
@RequiredArgsConstructor
@Slf4j
@Transactional
public class EmployeeEventRepositoryTestHelper
{
    private final EmployeeEventRepository employeeEventRepository;

    public void cleanDatabase()
    {
        LOGGER.info("Cleaning the employee event repository");
        employeeEventRepository.deleteAll();
    }
}
