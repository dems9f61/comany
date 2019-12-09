package com.takeaway.employeeservice;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * User: StMinko Date: 07.08.2019 Time: 12:43
 *
 * <p>
 */
@DisplayName("Context loading integration test")
class ContextLoadTest extends IntegrationTestSuite
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================
    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================
    // =================  protected/package local  Methods ===================

    @DisplayName("The spring related application context should be loaded")
    @Test
    void givenApplicationContext_whenLoad_thenPass() {}

    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
