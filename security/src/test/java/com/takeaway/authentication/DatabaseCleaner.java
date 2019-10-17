package com.takeaway.authentication;

import com.takeaway.authentication.permission.control.PermissionRepositoryTestHelper;
import com.takeaway.authentication.role.control.RoleRepositoryTestHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * User: StMinko
 * Date: 07.06.2019
 * Time: 12:07
 * <p/>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseCleaner
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================

    private final RoleRepositoryTestHelper       roleRepositoryTestHelper;
    private final PermissionRepositoryTestHelper permissionRepositoryTestHelper;

    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================

    void cleanDatabases()
    {
        LOGGER.info("Cleaning up the test database");
        permissionRepositoryTestHelper.cleanDatabase();
        roleRepositoryTestHelper.cleanDatabase();
    }

    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
