package com.takeaway.authentication.user.entity;

import com.takeaway.authentication.AbstractTestFactory;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

/**
 * User: StMinko Date: 21.10.2019 Time: 12:01
 *
 * <p>
 */
@Component
public class UserTestFactory extends AbstractTestFactory<User, UserTestFactory.Builder>
{
  public static final String PWD = "DEFAULT_PASSWORD";

  public Builder builder()
  {
    return new Builder();
  }

  public static class Builder implements AbstractTestFactory.Builder<User>
  {
    private String userName;

    private String oldPassword;

    private String newPassword;

    private String confirmPassword;

    Builder()
    {
      this.userName = RandomStringUtils.randomAlphabetic(10);
      this.oldPassword = PWD;
      this.newPassword = oldPassword;
      this.confirmPassword = newPassword;
    }

    public Builder userName(String userName)
    {
      this.userName = userName;
      return this;
    }

    public Builder oldPassword(String oldPassword)
    {
      this.oldPassword = oldPassword;
      return this;
    }

    public Builder newPassword(String newPassword)
    {
      this.newPassword = newPassword;
      return this;
    }

    public Builder confirmPassword(String confirmPassword)
    {
      this.confirmPassword = confirmPassword;
      return this;
    }

    @Override
    public User create()
    {
      User user = new User();
      user.setUserName(userName);
      user.setNewPassword(newPassword);
      user.setConfirmPassword(confirmPassword);
      user.setOldPassword(oldPassword);
      return user;
    }
  }
}
