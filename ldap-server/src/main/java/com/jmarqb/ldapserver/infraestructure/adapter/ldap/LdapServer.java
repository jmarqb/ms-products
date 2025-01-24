package com.jmarqb.ldapserver.infraestructure.adapter.ldap;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import lombok.extern.slf4j.Slf4j;

import com.unboundid.ldap.listener.InMemoryDirectoryServer;
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig;
import com.unboundid.ldap.listener.InMemoryListenerConfig;

@Slf4j
@Component
public class LdapServer implements CommandLineRunner {

	private final String ldapPassword;

	private final Integer ldapPort;
	private final Resource schema;

	public LdapServer(@Value("${ldap.port}") Integer ldapPort, @Value("${ldap.password}") String ldapPassword, @Value("classpath:schema.ldif") Resource schema) {
		this.ldapPassword = ldapPassword;
		this.ldapPort = ldapPort;
		this.schema = schema;
	}

	@Override
	public void run(String... args) throws Exception {
		var listener = InMemoryListenerConfig.createLDAPConfig("ldap", ldapPort);
		var config = new InMemoryDirectoryServerConfig("dc=jmarqb,dc=com");
		config.addAdditionalBindCredentials("cn=manager", ldapPassword);
		config.setListenerConfigs(listener);
		config.setReferentialIntegrityAttributes("member", "uniqueMember");

		var ds = new InMemoryDirectoryServer(config);
		ds.importFromLDIF(true, schema.getFile());
		ds.startListening();
		log.info("LDAP Server ready at port {}", ldapPort);
	}
}
