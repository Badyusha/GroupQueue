package com.project.groupqueue.utils;

import com.project.groupqueue.models.dto.Pair;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;

import javax.crypto.*;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.math.BigInteger;
import java.security.*;
import java.util.Base64;

public class EncryptionUtil {
	private static final String SECRET_KEY = System.getenv("SECRET_KEY");
	private static final String ENCRYPTION_ALGORITHM = System.getenv("ENCRYPTION_ALGORITHM");

	public static int[] convertByteArrayToIntArray(byte[] byteArray) {
		int[] intArray = new int[byteArray.length];
		for (int i = 0; i < byteArray.length; i++) {
			intArray[i] = byteArray[i] & 0xFF;
		}
		return intArray;
	}

	// cipher method
	public static String hashData(String message) {
		MessageDigest messageDigest = null;
		try {
			messageDigest = MessageDigest.getInstance("SHA-256");
		} catch (NoSuchAlgorithmException e) {
			System.err.println("Exception in Hash.hashData\nCannot getInstance of SHA-256");
			e.printStackTrace();
		}
		byte[] resultByteArray = messageDigest.digest(message.getBytes());
		return (new BigInteger(1, resultByteArray)).toString(16);
	}

	public static String encrypt(String algorithm, String input, SecretKey key, IvParameterSpec iv)
						 throws NoSuchPaddingException,
								NoSuchAlgorithmException,
								InvalidAlgorithmParameterException,
								InvalidKeyException,
								BadPaddingException,
								IllegalBlockSizeException {
		Cipher cipher = Cipher.getInstance(algorithm);

		cipher.init(Cipher.ENCRYPT_MODE, key, iv);
		byte[] cipherText = cipher.doFinal(input.getBytes());
		return Base64.getEncoder().encodeToString(cipherText);
	}

	public static String decrypt(String cipherText, IvParameterSpec iv) {
		byte[] plainText = new byte[0];
		try {
		Cipher cipher = Cipher.getInstance(ENCRYPTION_ALGORITHM);
		cipher.init(Cipher.DECRYPT_MODE, getSecretKey(), iv);
		plainText = cipher.doFinal(Base64.getDecoder()
				.decode(cipherText));
		} catch(NoSuchPaddingException |
				NoSuchAlgorithmException |
				InvalidAlgorithmParameterException |
				InvalidKeyException |
				BadPaddingException |
				IllegalBlockSizeException e) {
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

	public static IvParameterSpec getIv(HttpServletRequest request, String key) {
		for(Cookie cookie : request.getCookies()) {
			if(cookie.getName().equals(key + "IV")) {
				return new IvParameterSpec(Base64.getDecoder().decode(cookie.getValue().getBytes()));
			}
		}
		return null;
	}

	public static Pair<String, String> getEncryptedValueIVPair(String value) {
		try {
			SecretKey encryptionKey = getSecretKey();
			IvParameterSpec ivParameterSpec = EncryptionUtil.generateIv();

			String encryptedValue = EncryptionUtil.encrypt(ENCRYPTION_ALGORITHM, value, encryptionKey, ivParameterSpec);
			String encryptedIV = Base64.getEncoder().encodeToString(ivParameterSpec.getIV());

			return new Pair<>(encryptedValue, encryptedIV);
		} catch(InvalidAlgorithmParameterException | NoSuchPaddingException |
				IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException e)
		{
			System.err.println("Exception in CookieService.getEncryptedValue");
			e.printStackTrace();
		}
		return null;
	}

	public static SecretKey getSecretKey() {
		byte[] decodedKey = Base64.getDecoder().decode(SECRET_KEY);
		return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
	}
}
