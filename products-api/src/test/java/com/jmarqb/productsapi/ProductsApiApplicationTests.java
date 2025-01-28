package com.jmarqb.productsapi;

import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.oauth2.client.registration.InMemoryClientRegistrationRepository;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import org.junit.jupiter.api.Test;

@SpringBootTest
class ProductsApiApplicationTests {

	private @MockitoBean JwtDecoder jwtDecoder;
	private @MockitoBean InMemoryClientRegistrationRepository clientRegistrationRepository;

	@Test
	void contextLoads() {
	}

}
