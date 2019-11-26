package com.takeaway.authorization;

import com.takeaway.authorization.oauthclient.control.OAuthClientRepositoryTestHelper;
import com.takeaway.authorization.permission.control.PermissionRepositoryTestHelper;
import com.takeaway.authorization.role.control.RoleRepositoryTestHelper;
import com.takeaway.authorization.rolepermission.control.RolePermissionRepositoryTestHelper;
import com.takeaway.authorization.user.control.UserRepositoryTestHelper;
import com.takeaway.authorization.userrole.control.UserRoleRepositoryTestHelper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

/**
 * User: StMinko Date: 07.06.2019 Time: 12:07
 *
 * <p>
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class DatabaseCleaner
{
  // =========================== Class Variables ===========================
  // =============================  Variables  =============================

  private final RoleRepositoryTestHelper roleRepositoryTestHelper;

  private final PermissionRepositoryTestHelper permissionRepositoryTestHelper;

  private final RolePermissionRepositoryTestHelper rolePermissionRepositoryTestHelper;

  private final UserRepositoryTestHelper userRepositoryTestHelper;

    private final UserRoleRepositoryTestHelper userRoleRepositoryTestHelper;

    private final OAuthClientRepositoryTestHelper oAuthClientRepositoryTestHelper;

  // ============================  Constructors  ===========================
  // ===========================  public  Methods  =========================

  void cleanDatabases()
  {
    LOGGER.info("Cleaning up the test database");
    rolePermissionRepositoryTestHelper.cleanDatabase();
      userRoleRepositoryTestHelper.cleanDatabase();
    permissionRepositoryTestHelper.cleanDatabase();
    roleRepositoryTestHelper.cleanDatabase();
      userRepositoryTestHelper.cleanDatabase();
      oAuthClientRepositoryTestHelper.cleanDatabase();
  }

  // =================  protected/package local  Methods ===================
  // ===========================  private  Methods  ========================
  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}
