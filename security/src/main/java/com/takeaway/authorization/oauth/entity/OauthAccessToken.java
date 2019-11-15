package com.takeaway.authorization.oauth.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * User: StMinko Date: 06.11.2019 Time: 15:49
 *
 * <p>
 */
@Data
//@Entity
@Table(name = "oauth_access_token")
public class OauthAccessToken
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "oauth_access_token_sequence")
    @SequenceGenerator(name = "oauth_access_token_sequence",
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

    @Lob
    @Column(name = "authentication")
    private byte[] authentication;

    @Column(name = "refresh_token")
    private String refreshToken;

    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================
    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
