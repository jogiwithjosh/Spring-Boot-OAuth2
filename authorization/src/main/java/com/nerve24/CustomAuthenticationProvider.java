/**
 * 
 */
package com.nerve24;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import com.nerve24.entity.User;
import com.nerve24.entity.UserRole;
import com.nerve24.repository.UserRepository;

/**
 * @author JOGIREDDY
 *
 */
@Component
public class CustomAuthenticationProvider implements AuthenticationProvider{
	
	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private BCryptPasswordEncoder passwordEncoder;

	@Override
	public Authentication authenticate(Authentication auth) throws AuthenticationException {
		
		User user = null;
		try {
			user = userRepository.findByUsername(auth.getName());
			
			if(user == null){
				throw new BadCredentialsException("Username isn't registered!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw new BadCredentialsException("Something went wrong!");
		}
		
		if(!passwordEncoder.matches(auth.getCredentials().toString(), user.getPassword())){
			throw new BadCredentialsException("Username and Password desn't match!");
		}
		
		org.springframework.security.core.userdetails.User userPricipal = 
				new org.springframework.security.core.userdetails.User(auth.getName(), user.getPassword(), buildUserAuthority(user.getUserRole()));
		
		UsernamePasswordAuthenticationToken authToken = 
				new UsernamePasswordAuthenticationToken(userPricipal, user.getPassword(), userPricipal.getAuthorities());
		
		return authToken;
	}

	@Override
	public boolean supports(Class<?> clazz) {
		return clazz.equals(UsernamePasswordAuthenticationToken.class);
	}
	
	private List<GrantedAuthority> buildUserAuthority(Set<UserRole> userRoles) {
		Set<GrantedAuthority> setAuths = new HashSet<GrantedAuthority>();
		// Build user's authorities
		for (UserRole userRole : userRoles) {
			setAuths.add(new SimpleGrantedAuthority(userRole.getRole()));
		}
		List<GrantedAuthority> Result = new ArrayList<GrantedAuthority>(setAuths);
		return Result;
	}

}
