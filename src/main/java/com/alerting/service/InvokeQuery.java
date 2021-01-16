package com.alerting.service;

import com.alerting.domain.AlertQuery;
import com.alerting.domain.JWTToken;
import com.alerting.domain.LoginVM;
import com.alerting.domain.QueryResponse;
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
public class InvokeQuery {


    @Value("${woa-url}")
    public void setWoaUrl(String url) {
        hostUrl = url;
    }

    private static String hostUrl;

    private static InvokeQuery invoke;

    private QueryResponse queryResponse;

    InvokeQuery(){}

    private InvokeQuery(String token, AlertQuery query) throws URISyntaxException {
      if(hostUrl!=null){

            RestTemplate template = new RestTemplate();
            HttpHeaders headers = new HttpHeaders();
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

            final String countUrl = hostUrl+"/api/query/count";
            URI countUri = new URI(countUrl);
            headers.set("Authorization", "Bearer "+ token);
            HttpEntity<AlertQuery>  countEntity = new HttpEntity<>(query,headers);
            ResponseEntity<QueryResponse> countService = template.postForEntity(countUri, countEntity, QueryResponse.class);
            queryResponse = countService.getBody();
      }
    }

    public static QueryResponse getQueryResponse(String token, AlertQuery query) throws URISyntaxException {
        if(invoke==null){
            InvokeQuery woa = new InvokeQuery(token,query);
            return woa.queryResponse;
        }
        return invoke.queryResponse;
    }
}
