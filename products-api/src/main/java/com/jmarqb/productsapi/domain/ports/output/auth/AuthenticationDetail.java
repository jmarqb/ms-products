package com.jmarqb.productsapi.domain.ports.output.auth;

public record AuthenticationDetail(String access_token, String refresh_token, String scope, String id_token,
												String token_type,
												int expires_in) {
}
