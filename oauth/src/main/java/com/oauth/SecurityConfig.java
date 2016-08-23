package com.oauth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

import java.util.ArrayList;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Override
    @Autowired
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication()
                .withUser("user").password("password").roles("USER")
                .and()
                .withUser("admin").password("password").roles("USER", "ADMIN");
    }

    @Value("${fssa.ldap.protocol}")
    public String ldapProtocol;

    @Value("${fssa.ldap.server}")
    public String ldapServer;

    @Value("${fssa.ldap.port}")
    public String ldapPort;

    @Value("${fssa.ldap.baseDn}")
    public String ldapBaseDn;

    @Value("${fssa.ldap.uid}")
    public String ldapUid;

    @Value("${fssa.ldap.password}")
    public String ldapPassword;

    @Value("${fssa.ldap.searchbase}")
    public String ldapSearchBase;

    @Value("${tivoli.ldap.protocol}")
    public String tivoliLdapProtocol;

    @Value("${tivoli.ldap.server}")
    public String tivoliLdapServer;

    @Value("${tivoli.ldap.port}")
    public String tivoliLdapPort;

    @Value("${tivoli.ldap.baseDn}")
    public String tivoliLdapBaseDn;

    @Value("${tivoli.ldap.uid}")
    public String tivoliLdapUid;

    @Value("${tivoli.ldap.password}")
    public String tivoliLdapPassword;

    @Bean
    public AgpUserRoleServiceImpl userRoleService() {
        AgpUserRoleServiceImpl userRoleService = new AgpUserRoleServiceImpl();
        return userRoleService;
    }

    @Bean
    public AgpUserDetailsMapperImpl userMapper() {
        AgpUserDetailsMapperImpl userDetailsMapper = new AgpUserDetailsMapperImpl();
        userDetailsMapper.setUserRoleService(userRoleService());
        return userDetailsMapper;
    }

    @Bean
    public ApacheDSAuthenticationProvider apacheDSAuthProvider() {
        ApacheDSAuthenticationProvider apacheDSAuthenticationProvider = new ApacheDSAuthenticationProvider();
        apacheDSAuthenticationProvider.setPortalDirectoryService(portalDirectoryService());
        apacheDSAuthenticationProvider.setUserMapper(userMapper());
        apacheDSAuthenticationProvider.setPortalUserType("agency Portal");
        return apacheDSAuthenticationProvider;
    }

    @Bean
    public DefaultSpringSecurityContextSource fssaContextSource() {
        DefaultSpringSecurityContextSource defaultSpringSecurityContextSource =
                new DefaultSpringSecurityContextSource(ldapProtocol + "://" + ldapServer + ":" + ldapPort + "/" + ldapBaseDn);
        defaultSpringSecurityContextSource.setUserDn(ldapUid);
        defaultSpringSecurityContextSource.setPassword(ldapPassword);
        return defaultSpringSecurityContextSource;
    }

    @Bean
    public CCGUserRoleServiceImp ccgUserRoleService() {
        CCGUserRoleServiceImp ccgUserRoleServiceImp = new CCGUserRoleServiceImp();
        return ccgUserRoleServiceImp;
    }

    @Bean
    public LegacyRoleServiceImpl legacyRoleService() {
        LegacyRoleServiceImpl legacyRoleService = new LegacyRoleServiceImpl();
        return legacyRoleService;
    }

    @Bean
    public FilterBasedLdapUserSearch fssaLdapSearch() {
        FilterBasedLdapUserSearch filterBasedLdapUserSearch = new FilterBasedLdapUserSearch(ldapSearchBase, "(uid={0})", fssaContextSource());
        filterBasedLdapUserSearch.setSearchSubtree(true);
        filterBasedLdapUserSearch.setDerefLinkFlag(false);
        return filterBasedLdapUserSearch;
    }

    @Bean
    public ActiveDirectoryBindAuthenticator activeDirectoryBindAuthenticator() {
        ActiveDirectoryBindAuthenticator activeDirectoryBindAuthenticator = new ActiveDirectoryBindAuthenticator(fssaContextSource());
        activeDirectoryBindAuthenticator.setUserSearch(fssaLdapSearch());
        activeDirectoryBindAuthenticator.setAdminPassword(ldapPassword);
        activeDirectoryBindAuthenticator.setAdminUserDn(ldapUid);
        return activeDirectoryBindAuthenticator;
    }

    @Bean
    public PortalLdapAuthorityPopulator portalLdapAuthorityPopulatorCcg() {
        PortalLdapAuthorityPopulator portalLdapAuthorityPopulator = new PortalLdapAuthorityPopulator();
        portalLdapAuthorityPopulator.setUserRoleService(ccgUserRoleService());
        return portalLdapAuthorityPopulator;
    }

    @Bean CCGUserContextMapper ccgUserContextMapper() {
        CCGUserContextMapper ccgUserContextMapper = new CCGUserContextMapper();
        return ccgUserContextMapper;
    }

    @Bean
    public LdapAuthenticationProvider ldapAuthProvider() {
        LdapAuthenticationProvider ldapAuthenticationProvider = new LdapAuthenticationProvider(activeDirectoryBindAuthenticator(), portalLdapAuthorityPopulatorCcg());
        ldapAuthenticationProvider.setUserDetailsContextMapper(ccgUserContextMapper());
        return ldapAuthenticationProvider;
    }

    @Bean
    public DefaultSpringSecurityContextSource tivoliContextSource() {
        DefaultSpringSecurityContextSource defaultSpringSecurityContextSource =
                new DefaultSpringSecurityContextSource(tivoliLdapProtocol + "://" + tivoliLdapServer + ":" + tivoliLdapPort + "/" + tivoliLdapBaseDn);
        defaultSpringSecurityContextSource.setAdminPassword(tivoliLdapPassword);
        defaultSpringSecurityContextSource.setAdminUserDn(tivoliLdapUid);
        return defaultSpringSecurityContextSource;
    }

    @Bean
    public BindAuthenticator bindAuthenticator() {
        BindAuthenticator bindAuthenticator = new BindAuthenticator(tivoliContextSource());
        List<String> list = new ArrayList<>();
        list.add("cn={0}");
        list.add("ou=ext");
        list.add("ou=users");
        bindAuthenticator.setUserDbPatterns(list);
        return bindAuthenticator;
    }

    @Bean
    public PortalLdapAuthorityPopulator portalLdapAuthorityPopulatorTivoli() {
        PortalLdapAuthorityPopulator portalLdapAuthorityPopulator = new PortalLdapAuthorityPopulator();
        portalLdapAuthorityPopulator.setPortalUserRoleService(legacyRoleService());
        return portalLdapAuthorityPopulator;
    }

    @Bean
    public LdapAuthenticationProvider tivoliAuthProvider() {
        LdapAuthenticationProvider ldapAuthenticationProvider = new LdapAuthenticationProvider(bindAuthenticator(), portalLdapAuthorityPopulatorTivoli());
        ldapAuthenticationProvider.setUserDetailsContextMapper(ccgUserContextMapper());
        return ldapAuthenticationProvider;
    }

}
