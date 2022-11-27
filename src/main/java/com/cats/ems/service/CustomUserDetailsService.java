package com.cats.ems.service;

import java.util.Collection;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import com.cats.ems.dto.JwtPayload;
import com.cats.ems.serviceImpl.CustomUserDetailService;

@SuppressWarnings("serial")
public class CustomUserDetailsService extends User implements CustomUserDetailService{
	
	JwtPayload jwtPayload;
	
	public CustomUserDetailsService(String username, String password, boolean enabled, boolean accountNonExpired,
			boolean credentialsNonExpired, boolean accountNonLocked,
			Collection<? extends GrantedAuthority> authorities) {
		super(username, password, enabled, accountNonExpired, credentialsNonExpired, accountNonLocked, authorities);
 
	}

	public CustomUserDetailsService(String username, String password,
			Collection<? extends GrantedAuthority> authorities, JwtPayload jwtPayload) {
		super(username, password, authorities);
		this.jwtPayload = jwtPayload;
	}

	@Override
	public JwtPayload getJwtPayload() {
		return jwtPayload;
	}

	@Override
	public void setJwtPayload(JwtPayload jwtPayload) {
		this.jwtPayload = jwtPayload;
	}
	

	 

	 

	
	 

	 
}
