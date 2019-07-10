package br.com.ottimizza.application.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.Size;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "oauth_client_details")
@NoArgsConstructor
@AllArgsConstructor
public class OAuthClientDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @Getter
    @Setter
    @Column(name = "client_id", length = 256)
    private String clientId;

    @Getter
    @Setter
    @Column(name = "resource_ids", length = 256)
    private String resourceIds;

    @Getter
    @Setter
    @Column(name = "client_secret", length = 256)
    private String clientSecret;

    @Getter
    @Setter
    @Column(name = "scope", length = 256)
    private String scope;

    @Getter
    @Setter
    @Column(name = "authorized_grant_types", length = 256)
    private String authorizedGrantTypes;

    @Getter
    @Setter
    @Column(name = "web_server_redirect_uri", length = 256)
    private String webServerRedirectUri;

    @Getter
    @Setter
    @Column(name = "authorities", length = 256)
    private String authorities;

    @Getter
    @Setter
    @Column(name = "access_token_validity")
    private Long accessTokenValidity;

    @Getter
    @Setter
    @Column(name = "refresh_token_validity")
    private Long refreshTokenValidity;

    @Getter
    @Setter
    @Column(name = "additional_information", length = 4096)
    private String additionalInformation;

    @Getter
    @Setter
    @Column(name = "autoapprove", length = 4096)
    private String autoapprove;

}
