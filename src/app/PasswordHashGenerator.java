package app;

import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.PBEKeySpec;

class PasswordHashGenerator {

	private byte[] salt;
	private byte[] hash;

	PasswordHashGenerator(String password) {
		salt = new byte[16];
		SecureRandom random = new SecureRandom();
		random.nextBytes(salt);

		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt, 1000, 128);
		SecretKeyFactory f = null;
		try {
			f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		hash = null;
		try {
			if (f != null) {
				hash = f.generateSecret(spec).getEncoded();
			}
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}
	}

	String createNewHash(String salt, String password) {
		Base64.Decoder dec = Base64.getDecoder();
		this.salt = dec.decode(salt);

		KeySpec spec = new PBEKeySpec(password.toCharArray(), this.salt, 1000, 128);
		SecretKeyFactory f = null;
		try {
			f = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA1");
		} catch (NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		hash = null;
		try {
			if (f != null) {
				hash = f.generateSecret(spec).getEncoded();
			}
		} catch (InvalidKeySpecException e) {
			e.printStackTrace();
		}

		Base64.Encoder enc = Base64.getEncoder();

		return enc.encodeToString(hash);
	}

	String getHash() {
		Base64.Encoder enc = Base64.getEncoder();

		return enc.encodeToString(hash);
	}

	String getSalt() {
		Base64.Encoder enc = Base64.getEncoder();

		return enc.encodeToString(salt);
	}
}