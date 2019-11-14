package com.takeaway.authorization.oauth.entity;

import lombok.Data;

import javax.persistence.*;

/**
 * User: StMinko Date: 06.11.2019 Time: 15:28
 * <p>
 */
@Data
@Entity
@Table(name = "oauth_refresh_token")
public class OauthRefreshToken
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "oauth_refresh_token_sequence")
    @SequenceGenerator(name = "oauth_refresh_token_sequence",
            allocationSize = 1)
    private Long id;

    @Column(name = "token_id")
    private String tokenId;

    @Lob
    @Column(name = "token")
    private byte[] token;

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
