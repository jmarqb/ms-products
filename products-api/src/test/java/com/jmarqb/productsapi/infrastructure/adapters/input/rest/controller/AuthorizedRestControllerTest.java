package com.jmarqb.productsapi.infrastructure.adapters.input.rest.controller;

import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jmarqb.productsapi.application.ports.input.AuthenticationUseCase;
import com.jmarqb.productsapi.domain.ports.output.auth.AuthenticationDetail;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthorizedRestControllerTest {

	private MockMvc mockMvc;

	private @Mock AuthenticationUseCase authenticationUseCase;


	@Test
	void authenticationDetail() throws Exception {
		ObjectMapper objectMapper = new ObjectMapper();
		mockMvc = MockMvcBuilders.standaloneSetup(new AuthorizedRestController(authenticationUseCase)).build();

		AuthenticationDetail authenticationDetail = Instancio.create(AuthenticationDetail.class);

		String code = "code";

		when(authenticationUseCase.authenticationDetail(code)).thenReturn(authenticationDetail);

		mockMvc.perform(get("/v1/authorized").param("code", code))
			.andExpect(status().isOk())
			.andExpect(content().json(objectMapper.writeValueAsString(authenticationDetail)))
			.andExpect(content().contentType("application/json"));

		verify(authenticationUseCase).authenticationDetail(code);

	}
}