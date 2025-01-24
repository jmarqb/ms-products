package com.jmarqb.productsapi.infrastructure.adapters.input.rest.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;

import lombok.RequiredArgsConstructor;

import com.jmarqb.productsapi.application.ports.input.AuthenticationUseCase;
import com.jmarqb.productsapi.domain.ports.output.auth.AuthenticationDetail;
import com.jmarqb.productsapi.infrastructure.adapters.input.rest.ui.AuthorizedRestUI;

@RequiredArgsConstructor
@Controller
public class AuthorizedRestController implements AuthorizedRestUI {

	private final AuthenticationUseCase authenticationUseCase;

	@Override
	public ResponseEntity<AuthenticationDetail> authenticationDetail(String code) {
		return new ResponseEntity<>(authenticationUseCase.authenticationDetail(code), HttpStatus.OK);
	}
}
