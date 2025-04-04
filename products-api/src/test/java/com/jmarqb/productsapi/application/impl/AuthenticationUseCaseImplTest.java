package com.jmarqb.productsapi.application.impl;

import com.jmarqb.productsapi.domain.ports.output.auth.AuthenticationDetail;
import com.jmarqb.productsapi.domain.ports.output.auth.DelegateAuthentication;
import org.instancio.Instancio;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AuthenticationUseCaseImplTest {

	private @Mock DelegateAuthentication delegateAuthentication;
	private @InjectMocks AuthenticationUseCaseImpl authenticationUseCaseImpl;

	@Test
	void authenticationDetailReturnsCorrectDetail() {
		String code = "validCode";
		AuthenticationDetail expectedDetail = Instancio.create(AuthenticationDetail.class);
		when(delegateAuthentication.authenticationDetail(code)).thenReturn(expectedDetail);

		AuthenticationDetail result = authenticationUseCaseImpl.authenticationDetail(code);

		assertThat(result).isEqualTo(expectedDetail);
	}

	@Test
	void authenticationDetailReturnsNullForInvalidCode() {
		String code = "invalidCode";
		when(delegateAuthentication.authenticationDetail(code)).thenReturn(null);

		AuthenticationDetail result = authenticationUseCaseImpl.authenticationDetail(code);

		assertThat(result).isNull();
	}

	@Test
	void authenticationDetailHandlesEmptyCode() {
		String code = "";
		when(delegateAuthentication.authenticationDetail(code)).thenReturn(null);

		AuthenticationDetail result = authenticationUseCaseImpl.authenticationDetail(code);

		assertThat(result).isNull();
	}

	@Test
	void authenticationDetailHandlesNullCode() {
		String code = null;
		when(delegateAuthentication.authenticationDetail(code)).thenReturn(null);

		AuthenticationDetail result = authenticationUseCaseImpl.authenticationDetail(code);

		assertThat(result).isNull();
	}
}