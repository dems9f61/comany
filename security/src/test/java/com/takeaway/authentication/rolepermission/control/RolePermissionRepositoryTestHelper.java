package com.takeaway.authentication.rolepermission.control;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * User: StMinko
 * Date: 19.10.2019
 * Time: 17:44
 * <p/>
 */
@Component
@RequiredArgsConstructor
@Transactional
@Slf4j
public class RolePermissionRepositoryTestHelper
{
    private final RolePermissionRepository rolePermissionRepository;

    public void cleanDatabase()
    {
        LOGGER.info("Cleaning the Role Permission repository");
        rolePermissionRepository.deleteAll();
    }
}