package com.takeaway.authorization.role.control;

import com.takeaway.authorization.AbstractRepositoryTestHelper;
import com.takeaway.authorization.role.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

/**
 * User: StMinko Date: 15.10.2019 Time: 11:35
 *
 * <p>
 */
@Component
@Transactional
public class RoleRepositoryTestHelper extends AbstractRepositoryTestHelper<Role, UUID, RoleRepository>
{
    @Autowired
    public RoleRepositoryTestHelper(RoleRepository repository)
    {
        super(repository);
    }
}
