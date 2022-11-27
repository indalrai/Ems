package com.cats.ems.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.willDoNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.Optional;

import javax.servlet.http.HttpServletRequest;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.cats.ems.config.JwtTokenUtil;
import com.cats.ems.dto.JwtPayload;
import com.cats.ems.model.CredentialManager;
import com.cats.ems.model.Employee;
import com.cats.ems.model.JwtToken;
import com.cats.ems.model.UserRole;
import com.cats.ems.repo.CredentialsRepository;
import com.cats.ems.repo.JwtTokenRepository;

@ExtendWith(MockitoExtension.class)
public class JwtUserDetailsServiceTest {
	
	@Mock
	CredentialsRepository credentialsRepository;
	
	@Mock
	JwtTokenRepository jwtTokenRepository;
	
	@Mock
	JwtTokenUtil jwtTokenUtil;
	
	@InjectMocks
	JwtUserDetailsService jwtUserDetailsService;
	
	@Test
	void loadByUserName() {
		CredentialManager user=new CredentialManager();
		long mn=1234;
		user.setId(1);
		user.setLoginId("amit.kumar");
		user.setPassword("admin");
		Employee employee=new Employee();
		employee.setAddress("a");
		employee.setEmail("b");
		employee.setEmployeeId(1);
		employee.setMobile(mn);
		employee.setName("c");
		UserRole userRole=new UserRole();
		userRole.setName("d");
		userRole.setRoleId(mn);
		employee.setUserRole(userRole);
		user.setEmployee(employee);
		
		JwtPayload jwtPayload=new JwtPayload();
		jwtPayload.setEmployeeId(user.getEmployee().getEmployeeId());
		jwtPayload.setEmployeeAddress(user.getEmployee().getAddress());
		jwtPayload.setEmployeeEmail(user.getEmployee().getEmail());
		jwtPayload.setEmployeeMobileNo(user.getEmployee().getMobile());
		jwtPayload.setEmployeeName(user.getEmployee().getName());
		jwtPayload.setUserRole(user.getEmployee().getUserRole().getName());
		CustomUserDetailsService customUserDetailsService=new CustomUserDetailsService(user.getLoginId(), user.getPassword(), new ArrayList<>(), jwtPayload);
		when(credentialsRepository.findByLoginId("amit.kumar")).thenReturn(user);
		CustomUserDetailsService customUserDetailsService1=jwtUserDetailsService.loadUserByUsername("amit.kumar");
		assertThat(customUserDetailsService1).isEqualTo( customUserDetailsService);	
	}
	
	@Test
	void logout() {
		HttpServletRequest httpServletRequest = null;
		JwtToken jwtToken=new JwtToken();
		jwtToken.setEmployeeId(1);
		jwtToken.setJwtToken("abc");
		long id=1;
		Optional<JwtToken> optional = Optional.of(jwtToken);
		willDoNothing().given(jwtTokenRepository).deleteById(1l);
		when(jwtTokenUtil.getToken(httpServletRequest)).thenReturn(id);
		jwtUserDetailsService.logout(Mockito.any());
		verify(jwtTokenRepository, times(1)).deleteById(1l);
	}
}
