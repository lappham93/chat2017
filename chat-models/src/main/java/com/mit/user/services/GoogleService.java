package com.mit.user.services;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdTokenVerifier;
import com.google.api.client.googleapis.javanet.GoogleNetHttpTransport;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.mit.http.context.ApplicationContextProvider;
import com.mit.http.exception.TokenInvalidException;
import com.mit.user.entities.SocialProfile;
import com.mit.user.enums.SocialType;

/**
 * Created by Hung Le on 2/15/17.
 */

public class GoogleService extends SocialService {
    private final JsonFactory JSON_FACTORY = JacksonFactory.getDefaultInstance();
    private List<String> CLIENT_IDS = Arrays.asList(ApplicationContextProvider.getApplicationContext().getEnvironment().getProperty("google.clientIds").split(","));
//    private final String ISSUER = Application.EnvConfig.getProperty("google.issuer");

    private GoogleIdToken.Payload payload;

    public GoogleService(String token) {
        super(token);
    }

    @Override
    public SocialProfile getProfile() throws Exception {
        Map<String, String> tokenInfo = getTokenInfo();
        String iss = tokenInfo.get("iss");
        String clientId = tokenInfo.get("aud");
        if (CLIENT_IDS.contains(clientId)) {
            HttpTransport transport = GoogleNetHttpTransport.newTrustedTransport();
            GoogleIdTokenVerifier verifier = new GoogleIdTokenVerifier.Builder(transport, JSON_FACTORY).setAudience(Arrays.asList(clientId)).setIssuer(iss).build();
            GoogleIdToken idToken = verifier.verify(getToken());
            if (idToken != null) {
                payload = idToken.getPayload();

                SocialProfile socialProfile = new SocialProfile();
                socialProfile.setId(payload.getSubject());
                socialProfile.setType(SocialType.GOOGLE.getType());
                socialProfile.setToken(token);
                socialProfile.setFirstName((String) payload.get("given_name"));
                socialProfile.setLastName((String) payload.get("family_name"));
                socialProfile.setEmail(payload.getEmail());

                String avatar = (String) payload.get("picture");
                if (!StringUtils.isEmpty(avatar)) {
                    if (avatar.indexOf('?') > 0) {
                        avatar = avatar.substring(0, avatar.indexOf('?'));
                    }
                    socialProfile.setAvatar(avatar + "?sz=800");
                }

                return socialProfile;
            }
        }
        throw new TokenInvalidException("Google Id Token Invalid");
    }

    private Map<String, String> getTokenInfo() throws Exception {
        try {
            String url = "https://www.googleapis.com/oauth2/v3/tokeninfo?id_token=" + getToken();
            RestTemplate restApi = new RestTemplate();
            ResponseEntity<Map> result = restApi.getForEntity(url, Map.class);
            if (result.getStatusCode().is2xxSuccessful()) {
                return result.getBody();
            } else {
                throw new TokenInvalidException("Google Id Token Invalid");
            }
        } catch (HttpClientErrorException e) {
            throw new TokenInvalidException(e.getResponseBodyAsString());
        }
    }
}
