package com.example.groupqueue.encryption;

import com.example.groupqueue.services.CookieService;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.IOException;
import java.math.BigInteger;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.KeySpec;
import java.util.Base64;

public class Encryption {
	// cipher method
	public static String hashData(String message) {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			System.err.println("Exception in Hash.hashData");
			e.printStackTrace();
		}

		byte[] resultByteArray = messageDigest.digest(message.getBytes());

		return (new BigInteger(1, resultByteArray)).toString(16);
	}

	public static String encrypt(String algorithm, String input, SecretKey key, IvParameterSpec iv)
			throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidAlgorithmParameterException,
					InvalidKeyException, BadPaddingException, IllegalBlockSizeException
	{
		Cipher cipher = Cipher.getInstance(algorithm);
		cipher.init(Cipher.ENCRYPT_MODE, key, iv);
		byte[] cipherText = cipher.doFinal(input.getBytes());
		return Base64.getEncoder()
				.encodeToString(cipherText);
	}

	public static String decrypt(String algorithm, String cipherText, SecretKey key, IvParameterSpec iv) {
		byte[] plainText = new byte[0];
		try {
		Cipher cipher = Cipher.getInstance(algorithm);
		cipher.init(Cipher.DECRYPT_MODE, key, iv);
		plainText = cipher.doFinal(Base64.getDecoder()
				.decode(cipherText));
		} catch(NoSuchPaddingException | NoSuchAlgorithmException | InvalidAlgorithmParameterException |
				InvalidKeyException | BadPaddingException | IllegalBlockSizeException e)
		{
			System.err.println("Exception in Encrypt.decrypt");
			e.printStackTrace();
		}
		return new String(plainText);
	}

	public static IvParameterSpec generateIv() {
		byte[] iv = new byte[16];
		new SecureRandom().nextBytes(iv);
		return new IvParameterSpec(iv);
	}

	public static SecretKey generateKey(int n) throws NoSuchAlgorithmException {
		KeyGenerator keyGenerator = KeyGenerator.getInstance("AES");
		keyGenerator.init(n);
		return keyGenerator.generateKey();
	}

	public static SecretKey getKeyFromPassword(String password, String salt)
			throws NoSuchAlgorithmException, InvalidKeySpecException
	{
		SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
		KeySpec spec = new PBEKeySpec(password.toCharArray(), salt.getBytes(), 65536, 256);
		return new SecretKeySpec(factory.generateSecret(spec)
				.getEncoded(), "AES");
	}
}
