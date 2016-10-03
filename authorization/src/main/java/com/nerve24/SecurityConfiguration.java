/**
 * 
 */
package com.nerve24;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

/**
 * @author JOGIREDDY
 *
 */
@Configuration
@EnableWebSecurity
@Order(-20)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter{
	
	@Autowired
	private CustomAuthenticationProvider authProvider;
	
	@Autowired
	private CustomAuthenticationEntryPoint customAuthenticationEntryPoint;
	
	@Autowired
	private CustomLogoutSuccessHandler customLogoutSuccessHandler;
	
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		http
		.csrf().disable()
				.exceptionHandling()
		        .authenticationEntryPoint(customAuthenticationEntryPoint)
		        .and()
		        .logout()
				.logoutRequestMatcher(new AntPathRequestMatcher("/logout"))
				.logoutSuccessUrl("/login")
				.invalidateHttpSession(true).clearAuthentication(true)
		        .logoutSuccessHandler(customLogoutSuccessHandler)
		        .and()
				.formLogin().permitAll()
				.and()
				.requestMatchers()
				.antMatchers("/", "/login","/logout", "/uaa/oauth/authorize", "/uaa/oauth/token")
				.and()
				.authorizeRequests()
				.anyRequest().permitAll()
				.and()
				.sessionManagement()
		        .sessionCreationPolicy(SessionCreationPolicy.IF_REQUIRED)
		        .and()
				.authorizeRequests()
				.anyRequest().authenticated();
	}

	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.authenticationProvider(authProvider);
	}	

}
