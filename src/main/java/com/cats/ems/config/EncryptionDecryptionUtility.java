package com.cats.ems.config;

import java.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.cats.ems.propertiesconfig.ConfigurationUrl;

@Component
public class EncryptionDecryptionUtility {


	private static  String key;

	private static String initVector ;
	private static  String decryptResult;
	private static String algorithm;
	@Autowired
	ConfigurationUrl configUrl;
	
  	
	

	public  String encrypt(String value) {
		try {
         
             key=configUrl.getKey();
             initVector=configUrl.getInitVector();
             algorithm=configUrl.getAlgorithm();
			IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
			SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

			Cipher cipher = Cipher.getInstance(algorithm);
			cipher.init(Cipher.ENCRYPT_MODE, skeySpec, iv);

			byte[] encrypted = cipher.doFinal(value.getBytes());
			return new String(Base64.getEncoder().encode(encrypted));
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	public  String decrypt(String encrypted) {
		try {
			 key=configUrl.getKey();
             initVector=configUrl.getInitVector();
             algorithm=configUrl.getAlgorithm();
			IvParameterSpec iv = new IvParameterSpec(initVector.getBytes("UTF-8"));
			SecretKeySpec skeySpec = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

			Cipher cipher = Cipher.getInstance(algorithm);
			cipher.init(Cipher.DECRYPT_MODE, skeySpec, iv);
			byte[] original = cipher.doFinal(Base64.getDecoder().decode(encrypted));
			decryptResult = new String(original);
			return decryptResult;
		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}
		}
