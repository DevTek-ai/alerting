package com.alerting.service;

import com.alerting.domain.JWTToken;
import com.alerting.domain.LoginVM;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.Arrays;

@Service
public class AuthenticateWOA {

    @Value("${woa-url}")
    public void setWoaUrl(String url) {
        hostUrl = url;
    }

    private static String hostUrl;

    @Value("${woa-user}")
    public void setWoaUser(String user) {
        loginUser = user;
    }
    private static String loginUser;

    @Value("${woa-pass}")
    public void setWoaPass(String pass) {
        loginPass = pass;
    }
    private static String loginPass;



     public AuthenticateWOA() {
    }

    public  String getAccessToken() throws URISyntaxException {
        if (hostUrl != null) {
            RestTemplate template = new RestTemplate();
            final String baseUrl = hostUrl + "/api/authenticate";
            URI uri = new URI(baseUrl);
            LoginVM login = new LoginVM(loginUser, loginPass);

            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

            HttpEntity<LoginVM> entity = new HttpEntity<>(login, headers);
            ResponseEntity<JWTToken> result = template.postForEntity(uri, entity, JWTToken.class);
            JWTToken token = result.getBody();

            return token.getIdToken();
        }
        return null;
    }


}
