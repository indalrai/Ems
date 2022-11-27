package com.cats.ems.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.cats.ems.propertiesconfig.ConfigurationUrl;

@Component
public class EMSPasswordEncoder implements PasswordEncoder {
	@Autowired
	EncryptionDecryptionUtility employeeEncryption;
	@Autowired
	ConfigurationUrl configurationUrl;

	@Override
	public String encode(CharSequence rawPassword) {

		String password = employeeEncryption.encrypt(String.valueOf(rawPassword));
		return password;
	}

	@Override
	public boolean matches(CharSequence rawPassword, String encodedPassword) {
		return encodedPassword.contentEquals(rawPassword);
	}

}
