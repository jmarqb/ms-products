package com.jmarqb.productsapi.application.impl;

import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;

import com.jmarqb.productsapi.application.ports.input.AuthenticationUseCase;
import com.jmarqb.productsapi.domain.ports.output.auth.AuthenticationDetail;
import com.jmarqb.productsapi.domain.ports.output.auth.DelegateAuthentication;

@RequiredArgsConstructor
@Component
public class AuthenticationUseCaseImpl implements AuthenticationUseCase {

	private final DelegateAuthentication delegateAuthentication;

	@Override
	public AuthenticationDetail authenticationDetail(String code) {
		return delegateAuthentication.authenticationDetail(code);
	}

}
