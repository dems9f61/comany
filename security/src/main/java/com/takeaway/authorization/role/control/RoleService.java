package com.takeaway.authorization.role.control;

import com.takeaway.authorization.integrationsupport.control.AbstractDefaultAuditedEntityService;
import com.takeaway.authorization.role.entity.Role;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.Validator;
import java.util.UUID;

/**
 * User: StMinko Date: 14.10.2019 Time: 13:45
 *
 * <p>
 */
@Service
public class RoleService extends AbstractDefaultAuditedEntityService<RoleRepository, Role, UUID>
{
  // =========================== Class Variables ===========================
  // =============================  Variables  =============================
  // ============================  Constructors  ===========================

  @Autowired
  public RoleService(RoleRepository repository, Validator validator)
  {
    super(repository, validator);
  }

  // ===========================  public  Methods  =========================
  // =================  protected/package local  Methods ===================
  // ===========================  private  Methods  ========================
  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}
