package com.takeaway.authentication.permission.entity;

import com.takeaway.authentication.AbstractTestFactory;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Component;

/**
 * User: StMinko Date: 17.10.2019 Time: 12:12
 *
 * <p>
 */
@Component
public class PermissionTestFactory extends AbstractTestFactory<Permission, PermissionTestFactory.Builder>
{
  public PermissionTestFactory.Builder builder()
  {
    return new PermissionTestFactory.Builder();
  }

  public static class Builder implements AbstractTestFactory.Builder<Permission>
  {
    private String name;

    private String description;

    Builder()
    {
      this.name = RandomStringUtils.randomAlphabetic(12);
      this.description = RandomStringUtils.randomAlphabetic(40);
    }

    public PermissionTestFactory.Builder name(String name)
    {
      this.name = name;
      return this;
    }

    public PermissionTestFactory.Builder description(String description)
    {
      this.description = description;
      return this;
    }

    public Permission create()
    {
        Permission permission = new Permission();
        return permission.setName(name)
                         .setDescription(description);
    }
  }
}
