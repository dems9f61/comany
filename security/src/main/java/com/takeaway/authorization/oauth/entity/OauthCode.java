package com.takeaway.authorization.oauth.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * User: StMinko Date: 06.11.2019 Time: 15:55
 *
 * <p>
 */
@Data
@Entity
@Table(name = "oauth_code")
public class OauthCode
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "oauth_code_sequence")
    @SequenceGenerator(name = "oauth_code_sequence",
            allocationSize = 1)
    private Long id;

    @Column(name = "code")
    private String code;

    @Lob
    @Column(name = "authentication")
    private byte[] authentication;

    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================
    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
