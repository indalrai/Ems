package com.cats.ems.serviceImpl;

import javax.servlet.http.HttpServletRequest;

import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.cats.ems.dto.CredentialManagerDTO;
import com.cats.ems.model.CredentialManager;
import com.cats.ems.service.CustomUserDetailsService;

public interface JwtUserDetailService {

	CustomUserDetailsService loadUserByUsername(String userId) throws UsernameNotFoundException;

	CredentialManager save(CredentialManagerDTO credentialManagerDTO);

	public void logout(HttpServletRequest httpServletRequest);

}