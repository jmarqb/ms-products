package com.jmarqb.productsapi.infrastructure.adapters.input.rest.ui;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.jmarqb.productsapi.domain.ports.output.auth.AuthenticationDetail;

@RestController
@RequestMapping("/v1")
public interface AuthorizedRestUI {

	@ResponseStatus(HttpStatus.OK)
	@GetMapping("/authorized")
	ResponseEntity<AuthenticationDetail> authenticationDetail(@RequestParam String code);

}
