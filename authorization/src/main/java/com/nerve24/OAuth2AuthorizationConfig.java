/**
 * 
 */
package com.nerve24;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.client.ClientDetailsUserDetailsService;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

/**
 * @author JOGIREDDY
 *
 */

@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationConfig extends AuthorizationServerConfigurerAdapter{
	
	@Autowired
	private DataSource dataSource;

	@Autowired
	private AuthenticationManager authenticationManager;
	
	@Bean
	public JdbcClientDetailsService clientDetailsService(){
		
		JdbcClientDetailsService clientDetailsService = new JdbcClientDetailsService(dataSource);
		clientDetailsService.setPasswordEncoder(passwordEncoder());
		return clientDetailsService;
	}
	
	public ClientDetailsUserDetailsService clientDetailsUserService(){
		ClientDetailsUserDetailsService clientDetailsUserDetailsService = new ClientDetailsUserDetailsService(clientDetailsService());
		return clientDetailsUserDetailsService;
		
	}

	@Bean
	public JdbcTokenStore tokenStore() {
		return new JdbcTokenStore(dataSource);
	}

	@Bean
	protected AuthorizationCodeServices authorizationCodeServices() {
		System.out.println(dataSource.getClass());
		System.out.println(dataSource);
		return new JdbcAuthorizationCodeServices(dataSource);
	}

	@Bean
	BCryptPasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}

	@Override
	public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
		security
				.tokenKeyAccess("permitAll()")
				.checkTokenAccess("isAuthenticated()")
				.passwordEncoder(passwordEncoder());
	}

	@Override
	public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
		endpoints.authorizationCodeServices(authorizationCodeServices())
				.authenticationManager(authenticationManager)
				.tokenStore(tokenStore())
				.approvalStoreDisabled();
	}

	@Override
	public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
		//clients.jdbc(dataSource)
				//.passwordEncoder(passwordEncoder());
		clients.jdbc(dataSource)
		.passwordEncoder(passwordEncoder())
		.withClient("client")
		.authorizedGrantTypes("authorization_code", "client_credentials", 
				"refresh_token","password", "implicit")
		.authorities("ROLE_CLIENT","ROLE_USER")
		.resourceIds("apis")
		.scopes("read")
		.secret("secret")
		.accessTokenValiditySeconds(300);
	}

}
