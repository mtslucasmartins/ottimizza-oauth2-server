package br.com.ottimizza.application.controllers;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.annotation.Resource;
import javax.inject.Inject;
import javax.servlet.http.HttpServletRequest;

import org.springframework.security.oauth2.common.OAuth2AccessToken;
import org.springframework.security.oauth2.provider.endpoint.FrameworkEndpoint;
import org.springframework.security.oauth2.provider.token.ConsumerTokenServices;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RevokeTokenEndpoint {

    // @Resource(name = "tokenServices")
    @Resource(name = "tokenServices")
    ConsumerTokenServices tokenServices;

    @Resource(name = "tokenStore")
    TokenStore tokenStore;

    @ResponseBody
    @RequestMapping(method = RequestMethod.GET, value = "/tokens")
    public List<String> getTokensByClientId() {
        List<String> tokenValues = new ArrayList<String>();
        Collection<OAuth2AccessToken> tokens = tokenStore.findTokensByClientId("bussola-contabil-client");
        if (tokens != null) {
            for (OAuth2AccessToken token : tokens) {
                tokenValues.add(token.getValue());
            }
        }
        return tokenValues;
    }

    @ResponseBody
    @RequestMapping(method = RequestMethod.DELETE, value = "/oauth/revoke_token")
    public void revokeToken(HttpServletRequest request) {
        String authorization = request.getHeader("Authorization");
        if (authorization != null && authorization.contains("Bearer")) {
            String tokenId = authorization.replace("Bearer ", "");
            System.out.println(tokenServices.revokeToken(tokenId));
        }
    }
}