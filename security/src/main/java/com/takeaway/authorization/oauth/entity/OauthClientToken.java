package com.takeaway.authorization.oauth.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * User: StMinko Date: 06.11.2019 Time: 15:21
 *
 * <p>
 */
@Data
//@Entity
@Table(name = "oauth_client_token")
public class OauthClientToken
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "oauth_client_token_sequence")
    @SequenceGenerator(name = "oauth_client_token_sequence",
            allocationSize = 1)
    private Long id;

    @Column(name = "token_id")
    private String tokenId;

    @Lob
    @Column(name = "token")
    private byte[] token;

    @Column(name = "authentication_id")
    private String authenticationId;

    @Column(name = "user_name")
    private String userName;

    @Column(name = "client_id")
    private String clientId;

    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================
    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
