package com.neepanlokInfotech.nukkad_App.client;

import com.neepanlokInfotech.nukkad_App.exception.ExternalServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.*;

@Slf4j
@Component
public class GenericRestClient {

    private final RestTemplate restTemplate;

    @Autowired
    public GenericRestClient(RestTemplate restTemplate){
        this.restTemplate=restTemplate;
    }

    public <T,R> R postForEntity(String url,T requestBody, Class<R> responseType,String errorMessage) {
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);

            HttpEntity<T> requestEntity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<R> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, responseType);
            if (response.getBody() == null) {
                log.warn("{} returned empty body. URL: {}", errorMessage, url);
                throw new ExternalServiceException(errorMessage + " Empty response body");
            }
            log.debug("Response from{}: HTTP {}", url, response.getStatusCode());
            return response.getBody();
        } catch (RestClientException ex) {
            log.error("Error calling external API: {}-{}", url, ex.getMessage(), ex);
            throw new ExternalServiceException(errorMessage, ex);
        }
    }
}
