package com.takeaway.employeeservice.resttest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * User: StMinko
 * Date: 20.03.2019
 * Time: 00:46
 * <p/>
 */
@DisplayName("Rest tests for the employee service")
public class EmployeeRestTest extends RestTestSuite
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================
    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================
    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================

    @Nested
    @DisplayName("when create")
    class WhenCreate
    {
        @Test
        @DisplayName("POST: 'http://.../employees' returns BAD_REQUEST if the specified email is already used")
        void givenAlreadyUsedEmail_whenCreateUser_thenStatus400()
        {
            // Arrange

            // Act

            // Assert
        }
    }

    // ============================  End of class  ===========================
}
