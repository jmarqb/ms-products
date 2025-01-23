package com.jmarqb.productsapi.infrastructure.adapters.output.auth;

import com.jmarqb.productsapi.application.ports.output.auth.AuthenticationDetail;
import com.jmarqb.productsapi.application.ports.output.auth.DelegateAuthentication;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import java.nio.charset.StandardCharsets;

@Service
public class DelegateAuthenticationCaller implements DelegateAuthentication {

	private final String grantType;
	private final String redirectUri;
	private final RestClient authRestClient;

	public DelegateAuthenticationCaller(RestClient authRestClient,
                                        @Value("${spring.security.oauth2.client.registration.auth-api.redirect-uri}") String redirectUri,
                                        @Value("${spring.security.oauth2.client.registration.auth-api.authorization-grant-type}") String grantType) {
		this.grantType = grantType;
		this.redirectUri = redirectUri;
		this.authRestClient = authRestClient;
	}

	@Override
	public AuthenticationDetail authenticationDetail(String code) {
		System.out.println("code = " + code);
		System.out.println("grantType = " + grantType);
		System.out.println("redirectUri = " + redirectUri);
		var map = new LinkedMultiValueMap<String, String>();
		map.add("redirect_uri", redirectUri);
		map.add("grant_type", grantType);
		map.add("code", code);
		return authRestClient.post()
			.uri("/oauth2/token")
			.body(map)
			.retrieve()
			.onStatus(HttpStatusCode::is4xxClientError, (request, response) -> {
				throw new RestClientResponseException("Error occurred",
					response.getStatusCode(),
					response.getStatusText(),
					response.getHeaders(),
					response.getBody().readAllBytes(),
					StandardCharsets.UTF_8);
			})
			.body(AuthenticationDetail.class);
	}
}
