package com.jmarqb.auth.server.app.infraestructure.config;

import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.ldap.core.support.BaseLdapPathContextSource;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.ldap.authentication.BindAuthenticator;
import org.springframework.security.ldap.authentication.LdapAuthenticationProvider;
import org.springframework.security.ldap.authentication.PasswordComparisonAuthenticator;
import org.springframework.security.ldap.search.FilterBasedLdapUserSearch;
import org.springframework.security.ldap.userdetails.DefaultLdapAuthoritiesPopulator;
import org.springframework.security.oauth2.core.AuthorizationGrantType;
import org.springframework.security.oauth2.core.ClientAuthenticationMethod;
import org.springframework.security.oauth2.core.oidc.OidcScopes;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.server.authorization.OAuth2TokenType;
import org.springframework.security.oauth2.server.authorization.client.InMemoryRegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClient;
import org.springframework.security.oauth2.server.authorization.client.RegisteredClientRepository;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configuration.OAuth2AuthorizationServerConfiguration;
import org.springframework.security.oauth2.server.authorization.config.annotation.web.configurers.OAuth2AuthorizationServerConfigurer;
import org.springframework.security.oauth2.server.authorization.settings.AuthorizationServerSettings;
import org.springframework.security.oauth2.server.authorization.settings.ClientSettings;
import org.springframework.security.oauth2.server.authorization.settings.TokenSettings;
import org.springframework.security.oauth2.server.authorization.token.JwtEncodingContext;
import org.springframework.security.oauth2.server.authorization.token.OAuth2TokenCustomizer;
import org.springframework.security.oauth2.server.resource.web.access.BearerTokenAccessDeniedHandler;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.util.matcher.MediaTypeRequestMatcher;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.time.Duration;
import java.util.Collection;
import java.util.UUID;
import java.util.stream.Collectors;

@EnableWebSecurity
@Configuration
public class SecurityConfig {

    private final RSAPublicKey publicKey;
    private final RSAPrivateKey privateKey;

    public SecurityConfig(@Value("${jwt.public}") RSAPublicKey publicKey, @Value("${jwt.private}") RSAPrivateKey privateKey) {
        this.publicKey = publicKey;
        this.privateKey = privateKey;
    }

    @Bean
    SecurityFilterChain authorizationSecurityFilterChain(HttpSecurity http,
                                                         AuthenticationProvider ldapAuthenticationProvider, RegisteredClientRepository clients,
                                                         AuthorizationServerSettings authorizationServerSettings) throws Exception {
        http.authorizeHttpRequests(auth -> auth.anyRequest().authenticated())
                .formLogin(Customizer.withDefaults())
                .authenticationProvider(ldapAuthenticationProvider)
                .with(OAuth2AuthorizationServerConfigurer.authorizationServer(), authorizationServer -> authorizationServer
                        .authorizationServerSettings(authorizationServerSettings)
                        .registeredClientRepository(clients))
                .oauth2ResourceServer(resource -> resource.jwt(Customizer.withDefaults()))
                .exceptionHandling(exceptions -> exceptions
                        .defaultAuthenticationEntryPointFor(new LoginUrlAuthenticationEntryPoint("/login"),
                                new MediaTypeRequestMatcher(MediaType.TEXT_HTML))
                        .accessDeniedHandler(new BearerTokenAccessDeniedHandler()))
                .getConfigurer(OAuth2AuthorizationServerConfigurer.class)
                .oidc(Customizer.withDefaults());
        return http.build();
    }

    @Bean
    PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    RegisteredClientRepository registeredClientRepository(TokenSettings tokenSettings) {
        var oidcClient = RegisteredClient.withId(UUID.randomUUID().toString())
                .clientId("products-api")
                .clientSecret("$2a$10$ORZMInIdv9oHGnNAFnf00u4RFrsEZrN9BOoL0wcyB3T4raa7Ic49i")
                .clientAuthenticationMethod(ClientAuthenticationMethod.CLIENT_SECRET_BASIC)
                .authorizationGrantType(AuthorizationGrantType.AUTHORIZATION_CODE)
                .authorizationGrantType(AuthorizationGrantType.REFRESH_TOKEN)
                .redirectUri("http://localhost:8080/login/oauth2/code/auth-api")
                .redirectUri("http://localhost:8080/v1/authorized")
                .scope(OidcScopes.OPENID)
                .scope(OidcScopes.PROFILE)
                .tokenSettings(tokenSettings)
                .clientSettings(ClientSettings.builder().requireAuthorizationConsent(false).build())
                .build();
        return new InMemoryRegisteredClientRepository(oidcClient);
    }

    @Bean
    TokenSettings tokenSettings() {
        return TokenSettings.builder().accessTokenTimeToLive(Duration.ofDays(1))
                .refreshTokenTimeToLive(Duration.ofDays(1))
                .reuseRefreshTokens(true)
                .build();
    }

    @Bean
    AuthorizationServerSettings authorizationServerSettings() {
        return AuthorizationServerSettings.builder().build();
    }

    @Bean
    JwtDecoder jwtDecoder(JWKSource<SecurityContext> jwkSource) {
        return OAuth2AuthorizationServerConfiguration.jwtDecoder(jwkSource);
    }

    @Bean
    JWKSource<SecurityContext> jwkSource() {
        RSAKey rsaKey = new RSAKey.Builder(publicKey).privateKey(privateKey).keyID("ms-auth").build();
        JWKSet jwkSet = new JWKSet(rsaKey);
        return new ImmutableJWKSet<>(jwkSet);
    }

    @Bean
    OAuth2TokenCustomizer<JwtEncodingContext> tokenCustomizer() {
        return context -> {
            var principal = context.getPrincipal();
            if (context.getTokenType().equals(OAuth2TokenType.ACCESS_TOKEN)) {
                context.getClaims().claim("authorities", getScopesFromPrincipal(principal));
            }
        };
    }

    @Bean
    AuthenticationProvider ldapAuthenticationProvider(BaseLdapPathContextSource ldapContextSource,
                                                      PasswordEncoder encoder, DefaultLdapAuthoritiesPopulator authoritiesPopulator) {
        var authenticator = new PasswordComparisonAuthenticator(ldapContextSource);
        authenticator.setUserDnPatterns(new String[]{"uid={0},ou=people"});
        authenticator.setPasswordEncoder(encoder);
        authenticator.setPasswordAttributeName("userPassword");
        return new LdapAuthenticationProvider(authenticator, authoritiesPopulator);
    }

    @Bean
    BindAuthenticator authenticator(BaseLdapPathContextSource ldapContextSource) {
        var search = new FilterBasedLdapUserSearch("ou=people", "(uid={0})", ldapContextSource);
        var authenticator = new BindAuthenticator(ldapContextSource);
        authenticator.setUserSearch(search);
        return authenticator;
    }

    @Bean
    DefaultLdapAuthoritiesPopulator authoritiesPopulator(BaseLdapPathContextSource ldapContextSource) {
        var populator = new DefaultLdapAuthoritiesPopulator(ldapContextSource, "ou=groups");
        populator.setGroupRoleAttribute("cn");
        populator.setGroupSearchFilter("(uniqueMember={0})");
        return populator;
    }

    private static Collection<String> getScopesFromPrincipal(Authentication principal) {
        return principal.getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toSet());
    }

}
