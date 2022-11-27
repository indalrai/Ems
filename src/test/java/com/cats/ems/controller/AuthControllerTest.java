package com.cats.ems.controller;

import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import java.util.Optional;
import javax.servlet.http.HttpServletRequest;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import com.cats.ems.config.JwtTokenUtil;
import com.cats.ems.model.JwtToken;
import com.cats.ems.serviceImpl.JwtUserDetailService;

@ExtendWith(MockitoExtension.class)
public class AuthControllerTest {
	
	@Mock
	JwtTokenUtil jwtTokenUtil;
	
	@Mock
	AuthenticationManager authenticationManager;
	
	@Mock
	JwtUserDetailService userDetails;
	
	@InjectMocks
	AuthController authController;
	
	
	@Test
	void createAuthenticationToken() throws Exception {
		when(userDetails.loadUserByUsername(Mockito.any())).thenReturn(Mockito.any());
		
		ResponseEntity<?> auth=authController.createAuthenticationToken("amit.kumar", null);
		
	}
	@Test
	void logout() {
		HttpServletRequest httpServletRequest = null;
		JwtToken jwtToken=new JwtToken();
		jwtToken.setEmployeeId(1);
		jwtToken.setJwtToken("abc");
		Optional<JwtToken> optional = Optional.of(jwtToken);
		willDoNothing().given(userDetails).logout(httpServletRequest);
		authController.logout(httpServletRequest);
		verify(userDetails, times(1)).logout(httpServletRequest);
	}
}
