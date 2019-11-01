package com.takeaway.authorization.permission.control;

import com.takeaway.authorization.AbstractRepositoryTestHelper;
import com.takeaway.authorization.permission.entity.Permission;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * User: StMinko Date: 17.10.2019 Time: 12:08
 *
 * <p>
 */
@Component
@Transactional
public class PermissionRepositoryTestHelper extends AbstractRepositoryTestHelper<Permission, UUID, PermissionRepository>
{
  @Autowired
  public PermissionRepositoryTestHelper(PermissionRepository repository)
  {
    super(repository);
  }
}
