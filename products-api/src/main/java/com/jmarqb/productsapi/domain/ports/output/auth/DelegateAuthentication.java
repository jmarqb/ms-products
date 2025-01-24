package com.jmarqb.productsapi.domain.ports.output.auth;

public interface DelegateAuthentication {
	AuthenticationDetail authenticationDetail(String code);
}
