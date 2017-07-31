package com.mit.user.services;

import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import com.mit.http.context.ApplicationContextProvider;
import com.mit.http.exception.TokenInvalidException;
import com.mit.user.bodies.PhoneAccount;
import com.mit.user.bodies.TokenExchange;
import com.mit.user.entities.SocialProfile;
import com.mit.user.enums.SocialType;
import com.mit.utils.HmacDigestUtils;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpMessageConverterExtractor;
import org.springframework.web.client.ResponseExtractor;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Hung Le on 2/15/17.
 */

public class AccountKitService extends SocialService {
    private String API_URL = ApplicationContextProvider.getApplicationContext().getEnvironment().getProperty("accountkit.graph.url");
    private String ACCESS_TOKEN = ApplicationContextProvider.getApplicationContext().getEnvironment().getProperty("accountkit.accessToken");
    private String APP_SECRET = ApplicationContextProvider.getApplicationContext().getEnvironment().getProperty("accountkit.appSecret");
    private String APP_SECRET_ROOT = HmacDigestUtils.sign(APP_SECRET, ACCESS_TOKEN);

    public AccountKitService(String token) {
        super(token);
    }

    public TokenExchange getAccessToken(String authCode) {
        String url = API_URL + "access_token?grant_type={grant_type}&code={code}&access_token={access_token}"; //&appsecret_proof={appsecret_proof}
        RestTemplate restApi = new RestTemplate();
        Map<String, String> variables = new HashMap<>();
        variables.put("grant_type", "authorization_code");
        variables.put("code", authCode);
        variables.put("access_token", ACCESS_TOKEN);
//        variables.put("appsecret_proof", APP_SECRET_ROOT);
        ResponseEntity<TokenExchange> response = restApi.getForEntity(url, TokenExchange.class, variables);
        return response.getBody();
    }

    public PhoneAccount getAccount(String accessToken) {
        String url = API_URL + "me?access_token={access_token}&appsecret_proof={appsecret_proof}";
        RestTemplate restApi = new RestTemplate();
        Map<String, String> variables = new HashMap<>();
        variables.put("access_token", accessToken);

        String appSecrectRoot = HmacDigestUtils.signToHex(APP_SECRET, accessToken);
        variables.put("appsecret_proof", appSecrectRoot);
        ResponseEntity<PhoneAccount> response = restApi.getForEntity(url, PhoneAccount.class, variables);
        return response.getBody();
    }

    public String deleteAccount(String accountId) {
        String url = API_URL + accountId;
        RestTemplate restApi = new RestTemplate();
        Map<String, String> variables = new HashMap<>();
        variables.put("access_token", ACCESS_TOKEN);
        variables.put("appsecret_proof", APP_SECRET_ROOT);
        ResponseExtractor<ResponseEntity<String>> responseExtractor = new ResponseExtractor<ResponseEntity<String>>() {
            @Override
            public ResponseEntity<String> extractData(ClientHttpResponse response) throws IOException {
                HttpMessageConverterExtractor<String> extractor = new HttpMessageConverterExtractor<>(String.class, restApi.getMessageConverters());
                extractor.extractData(response);
                ResponseEntity<String> entity = new ResponseEntity<>(extractor.extractData(response), response.getHeaders(), response.getStatusCode());
                return entity;
            }
        };
        ResponseEntity<String> response = restApi.execute(url, HttpMethod.DELETE, null, responseExtractor, variables);
        return response.getBody();
    }

    @Override
    public SocialProfile getProfile() throws Exception {
        try {
            TokenExchange tokenEx = getAccessToken(token);

            PhoneAccount account = getAccount(tokenEx.getAccessToken());

            SocialProfile socialProfile = new SocialProfile();
            socialProfile.setId(account.getId());
            socialProfile.setType(SocialType.ACCOUNT_KIT.getType());
            socialProfile.setToken(token);
            if (account.getPhone() != null) {
//                String phoneNumber = account.getPhone().get("number");
//                PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
//                Phonenumber.PhoneNumber numberProto = phoneUtil.parse(phoneNumber, null);
//                socialProfile.setPhone(phoneUtil.format(numberProto, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL));
            	socialProfile.setPhone(account.getPhone().get("national_number"));
                socialProfile.setCountryCode("+" + account.getPhone().get("country_prefix"));
            } else {
                socialProfile.setEmail(account.getEmail().get("address"));
            }

            return socialProfile;
        } catch (HttpClientErrorException e) {
            throw new TokenInvalidException(e.getResponseBodyAsString());
        }

    }
}
