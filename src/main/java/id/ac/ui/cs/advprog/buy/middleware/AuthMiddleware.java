package id.ac.ui.cs.advprog.buy.middleware;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

public class AuthMiddleware {
    public static final String authUrl = "http://35.198.243.155/";
    public static String getUsernameFromToken(String token) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);
        try {
            ResponseEntity<String> response = restTemplate.exchange(authUrl + "/user/get-username",
                    HttpMethod.GET,
                    entity,
                    String.class
            );
            return response.getBody();
        } catch (RestClientException e) {
            System.err.println("Request failed: " + e.getMessage());
            return null;
        }
    }


    public static String getRoleFromToken(String token) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.add("Authorization", token);
        HttpEntity<String> entity = new HttpEntity<>(null, headers);

        try {
            ResponseEntity<String> response = restTemplate.exchange(
                    authUrl + "user/get-role",
                    HttpMethod.GET,
                    entity,
                    String.class
            );
            return response.getBody();
        } catch (RestClientException e) {
            System.err.println("Request failed: " + e.getMessage());
            return null;
        }
    }
}