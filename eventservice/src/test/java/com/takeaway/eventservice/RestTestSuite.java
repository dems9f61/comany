package com.takeaway.eventservice;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.HttpHeaders;

/**
 * User: StMinko
 * Date: 18.03.2019
 * Time: 23:04
 * <p/>
 */
public abstract class RestTestSuite extends IntegrationTestSuite
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================

    @Autowired
    protected TestRestTemplate testRestTemplate;

    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================
    // =================  protected/package local  Methods ===================

    protected HttpHeaders defaultHttpHeaders()
    {
        return new HttpHeaders();
    }

    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
