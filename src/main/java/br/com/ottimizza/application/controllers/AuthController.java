package br.com.ottimizza.application.controllers;

import java.io.IOException;
import java.security.Principal;
import java.text.MessageFormat;
import java.util.Base64;

import javax.inject.Inject;
import org.springframework.web.bind.annotation.RestController;

import br.com.ottimizza.application.domain.dtos.UserDTO;
import br.com.ottimizza.application.domain.responses.ErrorResponse;
import br.com.ottimizza.application.domain.responses.GenericPageableResponse;
import br.com.ottimizza.application.domain.responses.GenericResponse;
import br.com.ottimizza.application.model.OAuthClientAdditionalInformation;
import br.com.ottimizza.application.model.OAuthClientDetails;
import br.com.ottimizza.application.services.OAuthService;
import br.com.ottimizza.application.services.UserService;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@RestController
public class AuthController {

    @Inject
    private UserService userService;

    @Inject
    private OAuthService oauthService;

    @Value("${oauth2-config.server-url}")
    private String OAUTH2_SERVER_URL;

    @Value("${oauth2-config.client-id}")
    private String OAUTH2_CLIENT_ID;

    @Value("${oauth2-config.client-secret}")
    private String OAUTH2_CLIENT_SECRET;

    @GetMapping("/oauth/userinfo") // @formatter:off
    public ResponseEntity<?> getUserInfo(Principal principal) throws Exception {
        return ResponseEntity.ok(new GenericResponse<UserDTO>(
                UserDTO.fromEntityWithOrganization(userService.findByUsername(principal.getName()))
        ));
    }

    @GetMapping("/oauth/tokeninfo")
    public Principal getTokenInfo(Principal principal) {
        return principal;
    }

    @PostMapping("/auth/callback")
    public ResponseEntity<String> oauthCallback(@RequestParam("code") String code,
            @RequestParam("redirect_uri") String redirectUri) throws IOException {
        String credentials = OAUTH2_CLIENT_ID + ":" + OAUTH2_CLIENT_SECRET;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            String uri = MessageFormat.format("{0}/oauth/token?grant_type={1}&code={2}&redirect_uri={3}",
                    OAUTH2_SERVER_URL, "authorization_code", code, redirectUri);
            HttpPost httpPost = new HttpPost(uri);
            httpPost.setHeader("Authorization", "Basic " + encodedCredentials);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity responseEntity = httpResponse.getEntity();
            String response = EntityUtils.toString(responseEntity, "UTF-8");
            return ResponseEntity.ok(response);
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(401).body("{}");
        }
    }

    @PostMapping(value = "/auth/refresh", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> oauthRefresh(@RequestParam("refresh_token") String refreshToken,
            @RequestParam("client_id") String clientId) throws IOException {
        String credentials = OAUTH2_CLIENT_ID + ":" + OAUTH2_CLIENT_SECRET;
        String encodedCredentials = Base64.getEncoder().encodeToString(credentials.getBytes());
        try {
            HttpClient httpClient = HttpClientBuilder.create().build();
            URIBuilder uriBuilder = new URIBuilder(OAUTH2_SERVER_URL + "/oauth/token");
            uriBuilder.addParameter("refresh_token", refreshToken);
            uriBuilder.addParameter("grant_type", "refresh_token");
            HttpPost httpPost = new HttpPost(uriBuilder.build());
            httpPost.setHeader("Authorization", "Basic " + encodedCredentials);
            HttpResponse httpResponse = httpClient.execute(httpPost);
            HttpEntity responseEntity = httpResponse.getEntity();
            return ResponseEntity.ok(EntityUtils.toString(responseEntity, "UTF-8"));
        } catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(401).body("{}");
        }
    }

    @PostMapping("/api/v1/oauth_client_details")
    public ResponseEntity<?> createClient(@RequestBody OAuthClientAdditionalInformation additionalInformation, Principal principal) {
        try {
            return ResponseEntity.ok(oauthService.save(additionalInformation, principal));
        }  catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("internal_server_error", "Something wrong happened."));
        }
    }

    @GetMapping("/api/v1/oauth_client_details")
    public ResponseEntity<?> fetchAll(@RequestParam(name = "page_index", defaultValue = "0") int pageIndex,
                                      @RequestParam(name = "page_size", defaultValue = "10") int pageSize, Principal principal) {
        try {
            GenericPageableResponse<OAuthClientDetails> response = new GenericPageableResponse<OAuthClientDetails>(
                oauthService.fetchAll(pageIndex, pageSize, principal)
            );
            return ResponseEntity.ok(response);
        }  catch (Exception ex) {
            ex.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(new ErrorResponse("internal_server_error", "Something wrong happened."));
        }
    }

    @ResponseBody
    @GetMapping("/api/v1/oauth_client_details/{clientId}/access_tokens")
    public ResponseEntity<?> fetchAccessTokensByClientId(@PathVariable("clientId") String clientId, Principal principal) {
        GenericResponse<String> response = new GenericResponse<String>(oauthService.fetchAccessTokensByClientId(clientId));
        return ResponseEntity.ok(response);
    }
    
}
