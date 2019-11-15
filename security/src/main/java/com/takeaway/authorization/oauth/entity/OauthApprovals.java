package com.takeaway.authorization.oauth.entity;

import lombok.Data;

import javax.persistence.*;
import java.time.ZonedDateTime;

/**
 * User: StMinko Date: 06.11.2019 Time: 16:03
 *
 * <p>
 */
@Data
//@Entity
@Table(name = "oauth_approvals")
public class OauthApprovals
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "oauth_approvals_sequence")
    @SequenceGenerator(name = "oauth_approvals_sequence",
            allocationSize = 1)
    private Long id;

    @Column(name = "clientId")
    private String clientId;

    @Column(name = "userId")
    private String userId;

    @Column(name = "scope")
    private String scope;

    @Column(name = "status")
    private String status;

    @Column(name = "expiresAt")
    private ZonedDateTime expiresAt;

    @Column(name = "lastModifiedAt")
    private ZonedDateTime lastModifiedAt;

    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================
    // =================  protected/package local  Methods ===================
    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
