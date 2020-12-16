package com.alerting.service;

import com.alerting.domain.FirebaseData;
import com.alerting.domain.FirebaseDataDetail;
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
public class FirebaseHandler {

    public static void dispatch(String firebaseToken, String message, Long alertHistoryId) throws URISyntaxException {
        RestTemplate template = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        final String firebaseUrl = "https://fcm.googleapis.com/fcm/send";
        URI firebaseUri = new URI(firebaseUrl);
        headers.set("Authorization", "key=AAAAzgRdM5k:APA91bFEUHY1wqPFh6-waW12g4BuMSat7uP7ka5NYCOR1M7cpLmotwW-TVwjEjCzgfr7fmuVI_ZRO_UcUTAUexiPjsllwTzdlRQnzI3g-84f3DVcPJRAlNoSp4TzIVOBsT4FzMLzLh4Q");

        FirebaseDataDetail fbDetail = new FirebaseDataDetail("default",message,alertHistoryId,"default title",true,"high");
        FirebaseData fbData = new FirebaseData(firebaseToken,fbDetail);

        HttpEntity<FirebaseData>  firebaseEntity = new HttpEntity<>(fbData,headers);
        ResponseEntity<String> firebaseService = template.postForEntity(firebaseUri, firebaseEntity, String.class);
        System.out.println(firebaseService);


    }

}
