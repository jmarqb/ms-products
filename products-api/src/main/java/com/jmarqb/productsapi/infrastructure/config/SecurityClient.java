package com.jmarqb.productsapi.infrastructure.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestClient;

@Configuration
public class SecurityClient {

	private final String authServerUrl;
	private final String clientId;
	private final String clientSecret;

	public SecurityClient(@Value("${spring.security.oauth2.client.provider.spring.issuer-uri}") String authServerUrl,
						  @Value("${spring.security.oauth2.client.registration.auth-api.client-id}") String clientId,
						  @Value("${spring.security.oauth2.client.registration.auth-api.client-secret}") String clientSecret) {
		this.authServerUrl = authServerUrl;
		this.clientId = clientId;
		this.clientSecret = clientSecret;
	}

	@Bean
	RestClient authRestClient() {
		System.out.println("authServerUrl = " + authServerUrl);
		System.out.println("clientId = " + clientId);
		System.out.println("clientSecret = " + clientSecret);
		var defaultHeaders = new HttpHeaders();
		defaultHeaders.setContentType(MediaType.MULTIPART_FORM_DATA);
		defaultHeaders.setBasicAuth(clientId, clientSecret);
		return RestClient.create(authServerUrl).mutate().defaultHeaders(httpHeaders -> httpHeaders.putAll(defaultHeaders))
				.build();
	}

}
