package com.takeaway.authorization.oauth.entity;

import com.fasterxml.jackson.annotation.JsonView;
import com.google.common.collect.Sets;
import com.takeaway.authorization.json.boundary.DataView;
import com.takeaway.authorization.persistence.boundary.AuditedUUIDEntity;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.hibernate.envers.Audited;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.provider.ClientDetails;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.groups.Default;
import java.util.*;
import java.util.stream.Collectors;

/**
 * User: StMinko Date: 06.11.2019 Time: 10:07
 *
 * <p>
 */
@Audited
@Getter
@Setter
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
@Entity
@Table(name = "oauth_client_details",
        schema = "data",
        uniqueConstraints = @UniqueConstraint(name = "uk_clients_client_id",
                columnNames = "client_id"),
        indexes = @Index(name = "idx_clients_client_id",
                columnList = "client_id"))
public class OauthClient extends AuditedUUIDEntity implements ClientDetails
{
    // =========================== Class Variables ===========================
    // =============================  Variables  =============================

    @NotBlank(groups = { Default.class, DataView.GET.class, DataView.POST.class, DataView.PUT.class })
    @JsonView({ DataView.GET.class, DataView.POST.class, DataView.PUT.class, DataView.PATCH.class })
    @Column(name = "client_id")
    private String clientId;

    @JsonView({ DataView.GET.class, DataView.POST.class, DataView.PUT.class, DataView.PATCH.class })
    @Column(name = "resource_ids")
    private String clientResourceIds;

    @JsonView({ DataView.GET.class, DataView.POST.class, DataView.PUT.class, DataView.PATCH.class })
    @Column(name = "secret_required")
    private Boolean secretRequired;

    @NotBlank(groups = { Default.class, DataView.GET.class, DataView.POST.class, DataView.PUT.class })
    @JsonView({ DataView.GET.class, DataView.POST.class, DataView.PUT.class, DataView.PATCH.class })
    @Column(name = "client_secret")
    private String clientSecret;

    @JsonView({ DataView.GET.class, DataView.POST.class, DataView.PUT.class, DataView.PATCH.class })
    @Column(name = "scoped")
    private Boolean clientScoped;

    @JsonView({ DataView.GET.class, DataView.POST.class, DataView.PUT.class, DataView.PATCH.class })
    @NotBlank(groups = { Default.class, DataView.GET.class, DataView.POST.class, DataView.PUT.class })
    @Column(name = "scope")
    private String clientScope;

    @JsonView({ DataView.GET.class, DataView.POST.class, DataView.PUT.class, DataView.PATCH.class })
    @Column(name = "authorized_grant_types")
    private String clientAuthorizedGrantTypes;

    @JsonView({ DataView.GET.class, DataView.POST.class, DataView.PUT.class, DataView.PATCH.class })
    @Column(name = "web_server_redirect_uri")
    private String clientRegisteredRedirectUri;

    @JsonView({ DataView.GET.class, DataView.POST.class, DataView.PUT.class, DataView.PATCH.class })
    private String clientAuthorities;

    @JsonView({ DataView.GET.class, DataView.POST.class, DataView.PUT.class, DataView.PATCH.class })
    @NotNull(groups = { Default.class, DataView.GET.class, DataView.POST.class, DataView.PUT.class }) @Min(-1)
    // Three days as seconds - max life time for access token
    @Max(84600 * 3)
    @Column(name = "access_token_validity")
    private Integer accessTokenValidityInSeconds;

    @JsonView({ DataView.GET.class, DataView.POST.class, DataView.PUT.class, DataView.PATCH.class })
    @NotNull(groups = { Default.class, DataView.GET.class, DataView.POST.class, DataView.PUT.class }) @Min(-1)
    // Seven days as seconds - max life time for Refresh token
    @Max(84600 * 7)
    @Column(name = "refresh_token_validity")
    private Integer refreshTokenValidityInSeconds;

    // ============================  Constructors  ===========================
    // ===========================  public  Methods  =========================

    @Override
    public String getClientId()
    {
        return clientId;
    }

    public Set<String> getResourceIds()
    {
        if (clientAuthorities != null)
        {
            return Sets.newHashSet(clientResourceIds.split(","));
        }
        return Collections.emptySet();
    }

    @Override
    public boolean isSecretRequired()
    {
        return secretRequired;
    }

    @Override
    public String getClientSecret()
    {
        return clientSecret;
    }

    @Override
    public boolean isScoped()
    {
        return clientScoped;
    }

    public Set<String> getScope()
    {
        return Sets.newHashSet(clientScope.split(","));
    }

    public String getClientAuthorities()
    {
        return clientAuthorities;
    }

    public Set<String> getAuthorizedGrantTypes()
    {
        return Sets.newHashSet(clientAuthorizedGrantTypes.split(","));
    }

    public Set<String> getRegisteredRedirectUri()
    {
        return Sets.newHashSet(clientRegisteredRedirectUri);
    }

    public Collection<GrantedAuthority> getAuthorities()
    {
        if (clientAuthorities != null)
        {
            return Arrays.stream(clientAuthorities.split(","))
                         .map(authority -> new SimpleGrantedAuthority(!authority.startsWith("ROLE_") ? "ROLE_" + authority : authority))
                         .collect(Collectors.toSet());
        }
        return Collections.emptySet();
    }

    @Override
    public Integer getAccessTokenValiditySeconds()
    {
        return accessTokenValidityInSeconds;
    }

    @Override
    public Integer getRefreshTokenValiditySeconds()
    {
        return refreshTokenValidityInSeconds;
    }

    @Override
    public boolean isAutoApprove(String scope)
    {
        return Arrays.asList(scope.split(","))
                     .contains(scope);
    }

    @Override
    public Map<String, Object> getAdditionalInformation()
    {
        return Collections.emptyMap();
    }

    // =================  protected/package local  Methods ===================

    @Override
    protected void onPrePersist()
    {
        super.onPrePersist();
        if (clientScoped == null)
        {
            setClientScoped(true);
        }
        if (getSecretRequired() == null)
        {
            setSecretRequired(true);
        }
    }

    // ===========================  private  Methods  ========================
    // ============================  Inner Classes  ==========================
    // ============================  End of class  ===========================
}
