package com.code.monks.nukkad.client;

import com.code.monks.nukkad.exception.ExternalServiceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import org.springframework.http.*;

import java.util.Map;

@Slf4j
@Component
public class GenericRestClient {

	private final RestTemplate restTemplate;

	@Autowired
	public GenericRestClient(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public <T, R> R postForEntity(String url, T requestBody, Class<R> responseType, String errorMessage) {
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
		}
		catch (RestClientException ex) {
			log.error("Error calling external API: {}-{}", url, ex.getMessage(), ex);
			throw new ExternalServiceException(errorMessage, ex);
		}
	}


	public Map<String,Object> callApi(String url,String token){
		log.info("Calling external API: {}", url);

		//prepare headers with Bearer token
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		headers.setBearerAuth(token);

		HttpEntity<Void> entity = new HttpEntity<>(headers);

		try{
			ResponseEntity<Map> response = restTemplate.exchange(
					url,
					HttpMethod.POST,
					entity,
					Map.class
			);

			if (response.getStatusCode() == HttpStatus.OK){
				log.info("API call successful");
				return response.getBody();
			}else{
				log.error("API call faild with status: {}",response.getStatusCode());
				throw new RestClientException("API call failed with status: "+response.getStatusCode());
			}
		}catch (RestClientException e){
			log.error("Error calling external API: {}",url,e);
			throw e;
		}
	}

}
