package com.takeaway.authorization.persistence.boundary;

import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.history.RevisionRepository;

/**
 * User: StMinko Date: 16.10.2019 Time: 14:18
 *
 * <p>
 */
@NoRepositoryBean
public interface JpaAuditedSpecificationRepository<ENTITY, ID> extends JpaSpecificationRepository<ENTITY, ID>, RevisionRepository<ENTITY, ID, Long> {
  // =========================== Class Variables ===========================
  // ==============================  Methods  ==============================
  // ============================  Inner Classes  ==========================
  // ============================  End of class  ===========================
}
