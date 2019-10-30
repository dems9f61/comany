package com.takeaway.authentication.user.control;

import com.takeaway.authentication.integrationsupport.control.AbstractDefaultAuditedEntityService;
import com.takeaway.authentication.integrationsupport.entity.ServiceException;
import com.takeaway.authentication.permission.entity.Permission;
import com.takeaway.authentication.user.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.Validator;
import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.UUID;

import static com.takeaway.authentication.integrationsupport.entity.ServiceException.Reason.INVALID_PARAMETER;

/**
 * User: StMinko Date: 21.10.2019 Time: 10:49
 *
 * <p>
 */
@Slf4j
@Transactional
@Validated
@Service
public class UserService extends AbstractDefaultAuditedEntityService<UserRepository, User, UUID>
{
  // =========================== Class Variables ===========================
  // =============================  Variables  =============================

  private final PasswordEncoder passwordEncoder;

  // ============================  Constructors  ===========================

  @Autowired
  public UserService(UserRepository repository, Validator validator, PasswordEncoder passwordEncoder)
  {
    super(repository, validator);
    this.passwordEncoder = passwordEncoder;
  }

  // ===========================  public  Methods  =========================

  @Transactional(propagation = Propagation.SUPPORTS)
  public Optional<User> findByUserName(@NotNull String username)
  {
    return getRepository().findByUserName(username);
  }

    @Transactional(propagation = Propagation.SUPPORTS)
    public Page<Permission> findAllPermissionByUser(@NotNull UUID id, @NotNull Pageable pageable)
    {
        return getRepository().findAllPermissionsByUser(id, pageable);
    }

  // =================  protected/package local  Methods ===================

  /**
   * Set encoded password hash becore create of User
   *
   * @param create The {@link User} to create
   * @return The created {@link User} on success
   * @throws RuntimeException on failure
   */
  @Override
  protected User onBeforeCreate(User create) throws ServiceException
  {
    super.onBeforeCreate(create);
    String userName = create.getUserName();
      if (userName != null && findByUserName(userName).isPresent())
    {
      throw new ServiceException(INVALID_PARAMETER, "The specified username exists already");
    }
    if (create.getNewPassword() == null || create.getConfirmPassword() == null)
    {
      throw new ServiceException(INVALID_PARAMETER, "Password creation requires a new password and a confirm password matching each other");
    }
    if (!create.getNewPassword().equals(create.getConfirmPassword()))
    {
      throw new ServiceException(INVALID_PARAMETER, "New Password and Confirm Password do not match");
    }
    create.setPasswordHash(passwordEncoder.encode(create.getNewPassword()));
    return create;
  }

  /**
   * Set encoded password hash becore update of User
   *
   * @param update The {@link User} data to update
   * @return The updated {@link User} on success
   * @throws RuntimeException on failure
   */
  @Override
  protected User onBeforeUpdate(User existing, User update) throws ServiceException
  {
    super.onBeforeUpdate(existing, update);
    String userName = update.getUserName();
      if (userName != null && findByUserName(userName).isPresent())
    {
      throw new ServiceException(INVALID_PARAMETER, "The specified username exists already");
    }
    if (update.getOldPassword() != null)
    {
      if (!passwordEncoder.encode(update.getOldPassword()).equals(existing.getPasswordHash()))
      {
        throw new ServiceException(INVALID_PARAMETER, "Password update - Bad Credentials");
      }
      if (update.getNewPassword() == null || update.getConfirmPassword() == null)
      {
        throw new ServiceException(INVALID_PARAMETER, "Password update - Bad Credentials");
      }
      if (!update.getNewPassword().equals(update.getConfirmPassword()))
      {
        throw new ServiceException(INVALID_PARAMETER, "Password update - Bad Credentials");
      }
      update.setPasswordHash(passwordEncoder.encode(update.getNewPassword()));
    }
    return update;
  }
  // ===========================  private  Methods  ========================
  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}
