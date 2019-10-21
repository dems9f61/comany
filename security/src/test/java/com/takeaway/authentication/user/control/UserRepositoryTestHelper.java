package com.takeaway.authentication.user.control;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

/**
 * User: StMinko
 * Date: 21.10.2019
 * Time: 11:53
 * <p/>
 */
@Component
@RequiredArgsConstructor
@Transactional
@Slf4j
public class UserRepositoryTestHelper
{
    private final UserRepository userRepository;

     public void cleanDatabase()
     {
         LOGGER.info("Cleaning the User repository");
         userRepository.deleteAll();
     }
}