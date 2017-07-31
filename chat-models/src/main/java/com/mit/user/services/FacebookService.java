package com.mit.user.services;

import java.util.HashMap;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.RestTemplate;

import com.mit.http.context.ApplicationContextProvider;
import com.mit.http.exception.TokenInvalidException;
import com.mit.user.bodies.FBUser;
import com.mit.user.entities.SocialProfile;

/**
 * Created by Hung Le on 2/15/17.
 */

public class FacebookService extends SocialService {
    private String API_URL = ApplicationContextProvider.getApplicationContext().getEnvironment().getProperty("facebook.graph.url");
    private String APP_ID = ApplicationContextProvider.getApplicationContext().getEnvironment().getProperty("facebook.appId");
    private String ACCESS_TOKEN = ApplicationContextProvider.getApplicationContext().getEnvironment().getProperty("facebook.accessToken");
    private String APP_SECRET = ApplicationContextProvider.getApplicationContext().getEnvironment().getProperty("facebook.appSecret");
    
    public FacebookService(String token) {
        super(token);
    }

    private FBUser fbUser;

//    public FBDebugToken getFBDebugToken() throws Exception {
//        String url = API_URL + "debug_token/?access_token={access_token}&input_token={input_token}";
//        RestTemplate restApi = new RestTemplate();
//        Map<String, String> variables = new HashMap<>();
//        variables.put("access_token", ACCESS_TOKEN);
//        variables.put("input_token", getToken());
//
//        String appSecrectRoot = HmacDigestUtils.sign(APP_SECRET, getToken());
//        variables.put("appsecret_proof", appSecrectRoot);
//
//        FBDebugToken debugToken = null;
//
//        try {
//            ResponseEntity<FBDebugToken> response = restApi.getForEntity(url, FBDebugToken.class, variables);
//            if (response.getStatusCode().is2xxSuccessful()) {
//                debugToken = response.getBody();
//            }
//
//            if (debugToken != null && debugToken.getData() != null && APP_ID.equals(debugToken.getData().getAppId())) {
//                if (!debugToken.getData().isValid()) {
//                    throw new TokenInvalidException("FB Token Invalid");
//                }
//
//                if (System.currentTimeMillis() > debugToken.getData().getExpireTime() * 1000) {
//                    throw new TokenExpireException("FB token is expired");
//                }
//            } else {
//                throw new TokenInvalidException("FB Token Invalid");
//            }
//        } catch (HttpClientErrorException e) {
//            throw new TokenInvalidException(e.getResponseBodyAsString());
//        }
//
//        return debugToken;
//    }
    
    @Override
    public SocialProfile getProfile() throws Exception {
//        FBDebugToken debugToken = getFBDebugToken();

        String url = API_URL + "me/?fields={fields}&access_token={access_token}&token_for_business={token_for_business}";
        RestTemplate restApi = new RestTemplate();
        Map<String, String> variables = new HashMap<>();
        variables.put("fields", "id,first_name,last_name,birthday,picture.width(800).height(800),gender,interested_in,email");
        variables.put("access_token", getToken());
        variables.put("token_for_business", ACCESS_TOKEN);

        try {
            ResponseEntity<FBUser> response = restApi.getForEntity(url, FBUser.class, variables);
            if (response.getStatusCode().is2xxSuccessful()) {
                fbUser = response.getBody();
            }

            if (fbUser != null && fbUser.getId() != null) {
                SocialProfile socialProfile = fbUser.toSocialProfile();
                socialProfile.setToken(token);
                return socialProfile;
            } else {
                throw new TokenInvalidException("FB Token Invalid");
            }
        } catch (HttpClientErrorException e) {
            throw new TokenInvalidException(e.getResponseBodyAsString());
        }
    }
}
