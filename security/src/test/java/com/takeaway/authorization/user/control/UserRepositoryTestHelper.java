package com.takeaway.authorization.user.control;

import com.takeaway.authorization.AbstractRepositoryTestHelper;
import com.takeaway.authorization.user.entity.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.UUID;

/**
 * User: StMinko Date: 21.10.2019 Time: 11:53
 *
 * <p>
 */
@Component
public class UserRepositoryTestHelper extends AbstractRepositoryTestHelper<User, UUID, UserRepository>
{
    @Autowired
    public UserRepositoryTestHelper(UserRepository repository)
    {
        super(repository);
    }
}
