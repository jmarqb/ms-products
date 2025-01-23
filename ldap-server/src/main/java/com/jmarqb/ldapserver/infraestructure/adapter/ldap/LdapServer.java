package com.jmarqb.ldapserver.infraestructure.adapter.ldap;

import com.unboundid.ldap.listener.InMemoryDirectoryServer;
import com.unboundid.ldap.listener.InMemoryDirectoryServerConfig;
import com.unboundid.ldap.listener.InMemoryListenerConfig;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class LdapServer implements CommandLineRunner {

    private final Integer ldapPort;
    private final Resource schema;

    public LdapServer(@Value("${ldap.port}") Integer ldapPort, @Value("classpath:schema.ldif") Resource schema) {
        this.ldapPort = ldapPort;
        this.schema = schema;
    }

    @Override
    public void run(String... args) throws Exception {
        var listener = InMemoryListenerConfig.createLDAPConfig("ldap", ldapPort);
        var config = new InMemoryDirectoryServerConfig("dc=jmarqb,dc=com");
        config.addAdditionalBindCredentials("cn=manager", "password");
        config.setListenerConfigs(listener);
        config.setReferentialIntegrityAttributes("member", "uniqueMember");

        var ds = new InMemoryDirectoryServer(config);
        ds.importFromLDIF(true, schema.getFile());
        ds.startListening();
        log.info("LDAP Server ready at port {}", ldapPort);
    }
}
