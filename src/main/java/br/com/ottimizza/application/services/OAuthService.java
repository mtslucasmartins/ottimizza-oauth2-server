package br.com.ottimizza.application.services;

import java.util.Random;

import javax.inject.Inject;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.ottimizza.application.domain.responses.GenericResponse;
import br.com.ottimizza.application.model.OAuthClientAdditionalInformation;
import br.com.ottimizza.application.model.OAuthClientDetails;
import br.com.ottimizza.application.model.user.User;
import br.com.ottimizza.application.repositories.clients.OAuthClientRepository;

@Service
public class OAuthService {

    @Inject
    OAuthClientRepository authClientRepository;

    public GenericResponse<OAuthClientDetails> save(OAuthClientAdditionalInformation additionalInformation,
            User authorizedUser) {
        try {
            OAuthClientDetails oauthClientDetails = new OAuthClientDetails();

            String clientId = getRandomHexString(20);
            String clientSecret = getRandomHexString(40);

            additionalInformation.setClientId(clientId);
            additionalInformation.setClientSecret(clientSecret);

            String additionalInfo = new ObjectMapper().writeValueAsString(additionalInformation);

            oauthClientDetails.setClientId(clientId);
            oauthClientDetails.setClientSecret(new BCryptPasswordEncoder().encode(clientSecret));
            oauthClientDetails.setAdditionalInformation(additionalInfo);
            oauthClientDetails.setWebServerRedirectUri(additionalInformation.getRedirectUris());
            OAuthClientDetails registered = authClientRepository.save(oauthClientDetails);

            return new GenericResponse<OAuthClientDetails>(registered);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return new GenericResponse<OAuthClientDetails>();
    }

    private String getRandomHexString(int numchars) {
        Random r = new Random();
        StringBuffer sb = new StringBuffer();
        while (sb.length() < numchars) {
            sb.append(Integer.toHexString(r.nextInt()));
        }
        return sb.toString().substring(0, numchars);
    }

}
