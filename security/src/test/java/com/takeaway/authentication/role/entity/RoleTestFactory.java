package com.takeaway.authentication.role.entity;

import com.takeaway.authentication.AbstractTestFactory;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

/**
 * User: StMinko Date: 15.10.2019 Time: 11:24
 *
 * <p>
 */
@Component
public class RoleTestFactory extends AbstractTestFactory<Role, RoleTestFactory.Builder>
{
  public Builder builder()
  {
    return new Builder();
  }

  public static class Builder implements AbstractTestFactory.Builder<Role>
  {
    private String name;

    private String description;

    Builder()
    {
      this.name = RandomStringUtils.randomAlphabetic(12);
      this.description = RandomStringUtils.randomAlphabetic(40);
    }

    public Builder name(String name)
    {
      this.name = name;
      return this;
    }

    public Builder description(String description)
    {
      this.description = description;
      return this;
    }

    public Role create()
    {
      Role role = new Role();
      return role.setName(name).setDescription(description);
    }
  }
}
