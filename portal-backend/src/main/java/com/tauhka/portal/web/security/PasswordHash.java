package com.tauhka.portal.web.security;

import static com.tauhka.portal.util.Constants.LOG_PREFIX_PORTAL;

import java.nio.charset.StandardCharsets;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.spec.InvalidKeySpecException;
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

import com.tauhka.portal.ejb.UserEJB;
import com.tauhka.portal.util.Constants;
import com.tauhka.portal.web.exeptions.PasswordNotValidException;

// Partly read/used/combined from https://www.baeldung.com/java-password-hashing
public class PasswordHash {
	private static final Logger LOGGER = Logger.getLogger(UserEJB.class.getName());
	private String saltAsHex = null;
	private String passwordHash = null;

	public void generateHashes(String password) {
		this.reset();
		LOGGER.entering(LOG_PREFIX_PORTAL + PasswordHash.class.getName(), "generateHashes");
		if (password == null || password.length() < Constants.PASSWORD_MIN_LENGTH || password.length() > Constants.PASSWORD_MAX_LENGTH) {
			throw new PasswordNotValidException("password do not match criteria");
		}
		SecureRandom random = new SecureRandom();
		byte[] salt = new byte[16];
		char[] passarray = password.toCharArray();
		password = null;
		random.nextBytes(salt);
		PBEKeySpec spec = new PBEKeySpec(passarray, salt, 65536, 128);
		try {
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			byte[] hash = factory.generateSecret(spec).getEncoded();
			this.passwordHash = new String(hash, StandardCharsets.UTF_8);
			StringBuilder sb = new StringBuilder(spec.getSalt().length * 2);
			// To hex
			StringBuilder sb2 = new StringBuilder();
			for (byte b : salt) {
				sb2.append(b);
				sb.append(String.format("%02x", b));
			}
			this.saltAsHex = sb.toString();
			return;
		} catch (NoSuchAlgorithmException e) {
			LOGGER.log(Level.SEVERE, LOG_PREFIX_PORTAL + "pw error to hash password", e);
		} catch (InvalidKeySpecException s) {
			LOGGER.log(Level.SEVERE, LOG_PREFIX_PORTAL + "pw error to hash password2", s);
		} finally {
			spec.clearPassword();
			Arrays.fill(passarray, Character.MIN_VALUE);
		}
		LOGGER.exiting(LOG_PREFIX_PORTAL + PasswordHash.class.getName(), "generateHashes");
		throw new IllegalArgumentException("fail");
	}

	public boolean verifyHash(String password, String saltAsHex, String saltedPassWord) {
		this.reset();
		if (password == null || saltAsHex == null) {
			throw new PasswordNotValidException("password do not match criteria");
		}
		byte[] saltAsBytes = new byte[saltAsHex.length() / 2];
		for (int i = 0; i < saltAsBytes.length; i++) {
			saltAsBytes[i] = (byte) Integer.parseInt(saltAsHex.substring(2 * i, 2 * i + 2), 16);
		}

		char[] passarray = password.toCharArray();
		PBEKeySpec spec = new PBEKeySpec(passarray, saltAsBytes, 65536, 128);
		try {
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
			byte[] hash = factory.generateSecret(spec).getEncoded();
			this.passwordHash = new String(hash, StandardCharsets.UTF_8);
			return saltedPassWord.equals(this.passwordHash);
		} catch (NoSuchAlgorithmException e) {
			LOGGER.log(Level.SEVERE, "pw error to hash password", e);
		} catch (InvalidKeySpecException s) {
			LOGGER.log(Level.SEVERE, "pw error to hash password", s);
		} finally {
			spec.clearPassword();
			Arrays.fill(passarray, Character.MIN_VALUE);
		}
		throw new IllegalArgumentException("fail");
	}

	public void reset() {
		this.saltAsHex = null;
		this.passwordHash = null;
	}

	public String getSaltAsHex() {
		return saltAsHex;
	}

	public void setSaltAsHex(String saltAsHex) {
		this.saltAsHex = saltAsHex;
	}

	public String getPasswordHash() {
		return passwordHash;
	}

	public void setPasswordHash(String passwordHash) {
		this.passwordHash = passwordHash;
	}

}
