package com.example.groupqueue.encryption;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Hash {
	// cipher method
	public static String hashData(String message) throws NoSuchAlgorithmException {
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");

		byte[] resultByteArray = messageDigest.digest(message.getBytes());

		return (new BigInteger(1, resultByteArray)).toString(16);
	}
}
