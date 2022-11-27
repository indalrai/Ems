package com.cats.ems.service;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.cats.ems.config.JwtTokenUtil;
import com.cats.ems.dto.CredentialManagerDTO;
import com.cats.ems.dto.JwtPayload;
import com.cats.ems.model.CredentialManager;
import com.cats.ems.model.Employee;
import com.cats.ems.model.UserRole;
import com.cats.ems.repo.CredentialsRepository;
import com.cats.ems.repo.JwtTokenRepository;
import com.cats.ems.serviceImpl.JwtUserDetailService;

@Service
public class JwtUserDetailsService implements UserDetailsService, JwtUserDetailService {

	@Autowired
	private CredentialsRepository credentialsRepository;

	@Autowired
	JwtTokenRepository jwtTokenRepository;

	@Autowired
	JwtTokenUtil jwtTokenUtil;

	@SuppressWarnings("unused")
	@Override
	public CustomUserDetailsService loadUserByUsername(String userId) throws UsernameNotFoundException {
		CredentialManager user = credentialsRepository.findByLoginId(userId);

		JwtPayload jwtPayload = new JwtPayload();
		jwtPayload.setEmployeeId(user.getEmployee().getEmployeeId());
		jwtPayload.setEmployeeAddress(user.getEmployee().getAddress());
		jwtPayload.setEmployeeName(user.getEmployee().getName());
		jwtPayload.setEmployeeMobileNo(user.getEmployee().getMobile());
		jwtPayload.setUserRole(user.getEmployee().getUserRole().getName());
		jwtPayload.setEmployeeId(user.getEmployee().getEmployeeId());
		jwtPayload.setManagerId(user.getEmployee().getManagerId());
		if (user == null) {
			throw new UsernameNotFoundException("User not found with username: " + userId);
		}
		return new CustomUserDetailsService(user.getLoginId(), user.getPassword(), new ArrayList<>(), jwtPayload);
	}

	@Override
	public CredentialManager save(CredentialManagerDTO credentialManagerDTO) {

		CredentialManager credentialManager = new CredentialManager();
		credentialManager.setLoginId(credentialManagerDTO.getUserId());

		Employee employee = new Employee();
		employee.setName(credentialManagerDTO.getEmployeeDTO().getEmployeeName());
		employee.setEmail(credentialManagerDTO.getEmployeeDTO().getEmployeeEmail());
		employee.setAddress(credentialManagerDTO.getEmployeeDTO().getEmployeeAddress());
		employee.setMobile(credentialManagerDTO.getEmployeeDTO().getEmployeeMobileNo());
		UserRole userRole = new UserRole();
		userRole.setName(credentialManagerDTO.getEmployeeDTO().getUserRole().getName());
		employee.setUserRole(userRole);
		credentialManager.setEmployee(employee);
		return credentialsRepository.save(credentialManager);
	}

	@Override
	public void logout(HttpServletRequest httpServletRequest) {
		jwtTokenRepository.deleteById(jwtTokenUtil.getToken(httpServletRequest));
	}
}