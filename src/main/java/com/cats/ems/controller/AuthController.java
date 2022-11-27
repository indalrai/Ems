package com.cats.ems.controller;

import java.util.LinkedHashMap;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.cats.ems.advice.TrackExecutionTime;
import com.cats.ems.config.JwtTokenUtil;
import com.cats.ems.dto.CredentialManagerDTO;
import com.cats.ems.model.JwtRequest;
import com.cats.ems.model.JwtResponse;
import com.cats.ems.service.CustomUserDetailsService;
import com.cats.ems.serviceImpl.JwtUserDetailService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@SecurityRequirement(name = "cats.emsapi")
@CrossOrigin(value = "*")
@RequestMapping("/auth")
public class AuthController {

	@Autowired
	private AuthenticationManager authenticationManager;

	@Autowired
	private JwtTokenUtil jwtTokenUtil;

	private static final Logger LOGGER = LogManager.getLogger(AuthController.class);

	@Autowired
	JwtUserDetailService userDetails;

	@PostMapping("/login")
	@Operation(summary = "${login.message}")
	@TrackExecutionTime
	public ResponseEntity<?> createAuthenticationToken(@RequestHeader(value = "username") String username,
			@RequestHeader(value = "password") String password) throws Exception {
		JwtRequest authenticationRequest = new JwtRequest();
		authenticationRequest.setUsername(username);
		authenticationRequest.setPassword(password);

		LOGGER.debug("Token authenticated");
		authenticate(authenticationRequest.getUsername(), authenticationRequest.getPassword());

		final CustomUserDetailsService customUserDetailsService = userDetails
				.loadUserByUsername(authenticationRequest.getUsername());
		final String token = jwtTokenUtil.generateToken(customUserDetailsService);
		return ResponseEntity.ok(new JwtResponse(token));
	}

	@PostMapping("/register")
	@Operation(summary = "${register.message}")
	@TrackExecutionTime
	public ResponseEntity<?> saveUser(@RequestBody CredentialManagerDTO credentialManagerDTO) throws Exception {

		return ResponseEntity.ok(userDetails.save(credentialManagerDTO));
	}

	private void authenticate(String username, String password) throws Exception {
		try {
			authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
		} catch (DisabledException e) {
			throw new Exception("USER_DISABLED", e);
		} catch (BadCredentialsException e) {
			throw new Exception("INVALID_CREDENTIALS", e);
		}
	}

	@GetMapping("/logout")
	@Operation(summary = "${logout1.message}")
	@TrackExecutionTime
	public ResponseEntity<Map<Object, Object>> logout(HttpServletRequest httpServletRequest) {
		userDetails.logout(httpServletRequest);
		Map<Object, Object> eMap=new LinkedHashMap<>();
		eMap.put("Status", "Logout Successfully");
		return ResponseEntity.ok(eMap);
	}
}
