package com.code.monks.nukkad.client;

import com.code.monks.nukkad.exception.ExternalServiceException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

import static com.code.monks.nukkad.enums.ResponseErrorCodes.EXTERNAL_API_CALL_FAILED;
import static com.code.monks.nukkad.enums.ResponseErrorCodes.EXTERNAL_SERVICE_ERROR;

@Slf4j
@Component
public class GenericRestClient {

	private final RestTemplate restTemplate;

	@Autowired
	public GenericRestClient(RestTemplate restTemplate) {
		this.restTemplate = restTemplate;
	}

	public <T, R> R postForEntity(String url, T requestBody, Map<String, String> headers, Class<R> responseType, HttpMethod method) {
		try {
			HttpHeaders reqHeaders = new HttpHeaders();
			reqHeaders.setContentType(MediaType.APPLICATION_JSON);
			if (!CollectionUtils.isEmpty(headers)) {
				headers.forEach(reqHeaders::add);
			}


			log.info("Calling external API | method: {} | url: {}", method, url);

			HttpEntity<T> requestEntity = new HttpEntity<>(requestBody, reqHeaders);
			ResponseEntity<String> response = restTemplate.exchange(url, method, requestEntity, String.class);

			String responseBody = response.getBody();

			if (responseBody == null || responseBody.trim().isEmpty()) {
				log.warn("Empty response from external service: {}", url);
				throw new ExternalServiceException(EXTERNAL_SERVICE_ERROR, "Empty response body");
			}

			// Try parsing as error format first
			ObjectMapper mapper = new ObjectMapper();
			JsonNode node = mapper.readTree(responseBody);

			if (node.has("responseCode") && node.has("message") && node.size() == 2) {
				String errorCode = node.get("responseCode").asText();
				String message = node.get("message").asText();
				log.error("Received error from {} | code: {} | message: {}", url, errorCode, message);
				throw new ExternalServiceException(EXTERNAL_SERVICE_ERROR, errorCode + "::" + message);
			}

			// Else, treat as success
			R result = mapper.readValue(responseBody, responseType);
			return result;

		} catch (JsonProcessingException e) {
			log.error("Failed to parse response from {}: {}", url, e.getMessage());
			throw new ExternalServiceException(EXTERNAL_SERVICE_ERROR, "Failed to parse response");
		} catch (RestClientException ex) {
			log.error("Error calling external API: {} - {}", url, ex.getMessage(), ex);
			throw new ExternalServiceException(EXTERNAL_API_CALL_FAILED, ex);
		}
	}
}


