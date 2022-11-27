package com.cats.ems.config;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import com.cats.ems.dto.JwtPayload;
import com.cats.ems.model.JwtToken;
import com.cats.ems.repo.JwtTokenRepository;
import com.cats.ems.service.CustomUserDetailsService;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component
public class JwtTokenUtil implements Serializable {

	private static final long serialVersionUID = -2550185165626007488L;

	public static final long JWT_TOKEN_VALIDITY = 5 * 60 * 60;
	
	@Autowired
	JwtTokenRepository jwtTokenRepository;

	@Value("${jwt.secret}")
	private String secret;

	public String getUsernameFromToken(String token) {
		return getClaimFromToken(token, Claims::getSubject);
	}

	public JwtPayload getPayLoad(String token) {
		Claims claims = getAllClaimsFromToken(token);
		@SuppressWarnings("unchecked")
		Map<String, Object> payload = (Map<String, Object>) claims.get("PayLoad");
		ObjectMapper mapper = new ObjectMapper();
		JwtPayload jwtPayload = mapper.convertValue(payload, JwtPayload.class);
		return jwtPayload;
	}
	
	public long getToken(HttpServletRequest httpServletRequest) {
		final String requestTokenHeader = httpServletRequest.getHeader("Authorization");
		String jwtToken = null;
		long employeeId = 0;
		if (requestTokenHeader != null && requestTokenHeader.startsWith("Bearer ")) {
			jwtToken = requestTokenHeader.substring(7);
			try {
				JwtPayload jwtPayload=getPayLoad(jwtToken);
				employeeId=jwtPayload.getEmployeeId();
			}
			catch (Exception e) {
				e.printStackTrace();
			}
		}
		return employeeId;
	}

	public Date getIssuedAtDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getIssuedAt);
	}

	public Date getExpirationDateFromToken(String token) {
		return getClaimFromToken(token, Claims::getExpiration);
	}

	public <T> T getClaimFromToken(String token, Function<Claims, T> claimsResolver) {
		final Claims claims = getAllClaimsFromToken(token);
		return claimsResolver.apply(claims);
	}

	private Claims getAllClaimsFromToken(String token) {
		return Jwts.parser().setSigningKey(secret).parseClaimsJws(token).getBody();
	}

	private Boolean isTokenExpired(String token) {
		final Date expiration = getExpirationDateFromToken(token);
		return expiration.before(new Date());
	}

	private Boolean ignoreTokenExpiration(String token) {
		// here you specify tokens, for that the expiration is ignored
		return false;
	}

	public String generateToken(CustomUserDetailsService customUserDetailsService) {

		Map<String, Object> claims = new HashMap<>();
		
		return doGenerateToken(claims, customUserDetailsService);
	}

	private String doGenerateToken(Map<String, Object> claims, CustomUserDetailsService customUserDetailsService) {
		String jwtTokenString=Jwts.builder().setClaims(claims).setSubject(customUserDetailsService.getUsername())
				.claim("PayLoad", customUserDetailsService.getJwtPayload()).setIssuedAt(new Date(System.currentTimeMillis()))
				.setExpiration(new Date(System.currentTimeMillis() + JWT_TOKEN_VALIDITY * 1000))
				.signWith(SignatureAlgorithm.HS512, secret).compact();
		JwtToken jwtToken=new JwtToken();
		jwtToken.setJwtToken(jwtTokenString);
		jwtToken.setEmployeeId(customUserDetailsService.getJwtPayload().getEmployeeId());
		jwtTokenRepository.save(jwtToken);
		return jwtTokenString;
		
	}

	public Boolean canTokenBeRefreshed(String token) {
		return (!isTokenExpired(token) || ignoreTokenExpiration(token));
	}

	public Boolean validateToken(String token, UserDetails userDetails) {
		final String username = getUsernameFromToken(token);
		return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
	}

}
