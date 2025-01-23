package com.jmarqb.productsapi.application.ports.output.auth;

public interface DelegateAuthentication {
	AuthenticationDetail authenticationDetail(String code);
}
