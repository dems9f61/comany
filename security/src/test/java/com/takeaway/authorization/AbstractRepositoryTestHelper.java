package com.takeaway.authorization;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * User: StMinko Date: 29.10.2019 Time: 12:50
 *
 * <p>
 */
@Slf4j
@RequiredArgsConstructor
public abstract class AbstractRepositoryTestHelper<ENTITY, ID, REPOSITORY extends JpaRepository<ENTITY, ID>>
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================

    private final REPOSITORY repository;

    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================

    public final void cleanDatabase()
    {
        LOGGER.info("{}.cleanDatabase ()", this.getClass().getSimpleName());
        repository.deleteAll();
    }

    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
