package com.jmarqb.productsapi.infrastructure.adapters.output.auth;

import org.springframework.util.LinkedMultiValueMap;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.RestClientResponseException;

import com.jmarqb.productsapi.domain.ports.output.auth.AuthenticationDetail;
import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatExceptionOfType;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DelegateAuthenticationCallerTest {

	private @Mock RestClient authRestClient;
	private DelegateAuthenticationCaller caller;

	@BeforeEach
	void setUp() {
		caller = new DelegateAuthenticationCaller(authRestClient, "http://localhost:8080", "authorization_code");
	}

	@Test
	void authenticationDetailReturnsValidResponse() {
		var response = Instancio.of(AuthenticationDetail.class).create();
		mockRestClient(authRestClient, response);
		var result = caller.authenticationDetail("valid_code");
		assertThat(result).isEqualTo(response);
	}

	@Test
	void authenticationDetailThrowsExceptionForInvalidCode() {
		mockRestClientError(authRestClient);
		assertThatExceptionOfType(RestClientResponseException.class).isThrownBy(() -> caller.authenticationDetail("invalid_code"));
	}

	@Test
	void authenticationDetailHandlesNullCode() {
		mockRestClientError(authRestClient);
		assertThatExceptionOfType(RestClientResponseException.class).isThrownBy(() -> caller.authenticationDetail(null));
	}

	private static void mockRestClient(RestClient mocked, AuthenticationDetail value) {
		var response = mock(RestClient.ResponseSpec.class);
		baseMockRestClient(mocked, response);
		when(response.onStatus(any(), any())).thenReturn(response);
		when(response.body(AuthenticationDetail.class)).thenReturn(value);
	}

	private static void mockRestClientError(RestClient mocked) {
		var response = mock(RestClient.ResponseSpec.class);
		baseMockRestClient(mocked, response);
		when(response.onStatus(any(), any())).thenThrow(RestClientResponseException.class);
	}

	private static void baseMockRestClient(RestClient mocked, RestClient.ResponseSpec response) {
		var postRequest = mock(RestClient.RequestBodyUriSpec.class);
		when(mocked.post()).thenReturn(postRequest);
		when(postRequest.uri(anyString())).thenReturn(postRequest);
		when(postRequest.body(any(LinkedMultiValueMap.class))).thenReturn(postRequest);
		when(postRequest.retrieve()).thenReturn(response);
	}
}