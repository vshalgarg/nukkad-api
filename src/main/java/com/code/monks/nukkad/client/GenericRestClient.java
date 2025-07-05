package com.code.monks.nukkad.client;

import com.code.monks.nukkad.exception.ExternalServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.*;

import java.util.Collections;
import java.util.Map;

@Slf4j
@Component
public class GenericRestClient {

	private final RestTemplate restTemplate;

	@Autowired
	public GenericRestClient(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public <T, R> R postForEntity(String url, T requestBody,Map<String,String> headers, Class<R> responseType) {
		try {
			HttpHeaders reqHeaders = new HttpHeaders();
			reqHeaders.setContentType(MediaType.APPLICATION_JSON);
			if(!CollectionUtils.isEmpty(headers)){
				headers.forEach(reqHeaders::add);
			}

			HttpEntity<T> requestEntity = new HttpEntity<>(requestBody, reqHeaders);

			ResponseEntity<R> response = restTemplate.exchange(url, HttpMethod.POST, requestEntity, responseType);

			if (!response.getStatusCode().is2xxSuccessful()) {
				log.error("Non-successful response from {}: HTTP {}", url, response.getStatusCode());
				throw new ExternalServiceException("Received non-success HTTP status: " + response.getStatusCode());
			}
			if (response.getBody() == null) {
				log.warn("returned empty body. URL: {}",url);
				throw new ExternalServiceException("Empty response body");
			}
			log.debug("Response from{}: HTTP {}", url, response.getStatusCode());
			return response.getBody();
		}catch(HttpStatusCodeException ex){
			String errorBody = ex.getResponseBodyAsString();
			throw new ExternalServiceException(errorBody);
		}
		catch (RestClientException ex) {
			log.error("Error calling external API: {}-{}", url, ex.getMessage(), ex);
			throw new ExternalServiceException(ex.getMessage());
		}
	}
}


