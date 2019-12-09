package com.takeaway.authorization.runtime.persistence;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * User: StMinko Date: 14.10.2019 Time: 13:42
 *
 * <p>
 */
@NoRepositoryBean
public interface JpaSpecificationRepository<ENTITY, TYPE> extends JpaRepository<ENTITY, TYPE>, JpaSpecificationExecutor<ENTITY> {
    // =========================== Class Variables ===========================
    // ==============================  Methods  ==============================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
