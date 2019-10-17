package com.takeaway.authentication.permission.control;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * User: StMinko
 * Date: 17.10.2019
 * Time: 12:08
 * <p/>
 */
@Component
@RequiredArgsConstructor
@Transactional
@Slf4j
public class PermissionRepositoryTestHelper
{
    private final PermissionRepository permissionRepository;

    public void cleanDatabase()
    {
        LOGGER.info("Cleaning the Permission repository");
        permissionRepository.deleteAll();
    }
}