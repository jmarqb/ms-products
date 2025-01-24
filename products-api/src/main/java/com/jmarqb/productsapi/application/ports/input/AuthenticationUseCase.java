package com.jmarqb.productsapi.application.ports.input;


import com.jmarqb.productsapi.domain.ports.output.auth.AuthenticationDetail;

public interface AuthenticationUseCase {

	AuthenticationDetail authenticationDetail(String code);
}
