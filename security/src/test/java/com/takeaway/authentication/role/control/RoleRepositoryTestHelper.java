package com.takeaway.authentication.role.control;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * User: StMinko
 * Date: 15.10.2019
 * Time: 11:35
 * <p/>
 */
@Component
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RoleRepositoryTestHelper
{
    private final RoleRepository roleRepository;

    public void cleanDatabase()
    {
        LOGGER.info("Cleaning the Role repository");
        roleRepository.deleteAll();
    }
}