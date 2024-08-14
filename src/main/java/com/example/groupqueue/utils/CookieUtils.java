package com.example.groupqueue.utils;

import com.example.groupqueue.exceptions.CookieException;
import com.example.groupqueue.models.dto.Pair;
import com.example.groupqueue.models.dto.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import javax.crypto.BadPaddingException;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class CookieUtils {
	private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

	public static void deleteAllCookies(HttpServletResponse response, HttpServletRequest request) {
		Cookie[] cookies = request.getCookies();

		if(cookies == null) {
			return;
		}

		for(Cookie cookie : cookies) {
			deleteCookie(response, cookie.getName());
		}
	}

	public static void deleteCookie(HttpServletResponse response, String key) {
		Cookie cookie = new Cookie(key, null);
		cookie.setMaxAge(0);
		cookie.setSecure(true);
		cookie.setHttpOnly(true);
		cookie.setPath("/");

		response.addCookie(cookie);
	}

	public static void create(HttpServletResponse response, String key, String value) {
		Pair<String, String> encryptedValueIVPair = getEncryptedValueIVPair(value);

		String encryptedValue = encryptedValueIVPair.getFirst();
		if(encryptedValue == null) {
			throw new CookieException("cannot create cookie");
		}

		Cookie cookie = new Cookie(key, encryptedValue);
		cookie.setMaxAge(3600);
		cookie.setSecure(true);
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		response.addCookie(cookie);

		Cookie cookieIV = new Cookie(key + "IV", encryptedValueIVPair.getSecond());
		cookieIV.setMaxAge(3600);
		cookieIV.setSecure(true);
		cookieIV.setHttpOnly(true);
		cookieIV.setPath("/");

		response.addCookie(cookieIV);
	}

	public static String getCookie(HttpServletRequest request, String key) {
		Cookie[] cookies = request.getCookies();
		if(cookies == null) {
			throw new CookieException("cookie is null");
		}

		for(Cookie cookie : cookies) {
			String cookieName = cookie.getName();
			if(cookieName.equals(key)) {
				return EncryptionUtils.decrypt(ALGORITHM, cookie.getValue(), getSecretKey(), getIv(request, key));
			}
		}
		throw new CookieException("there is no " + key + " cookie");
	}

	public static void addRequired(HttpServletResponse response, User user) {
		create(response, "userId", user.getUserId().toString());
		create(response, "groupId", user.getGroupId().toString());
		create(response, "groupNumber", user.getGroupNumber().toString());
		create(response, "username", user.getUsername());
		create(response, "firstName", user.getFirstName());
		create(response, "lastName", user.getLastName());
	}

	/**
	 * method returns groupId from cookie if it exists, if it does not -> exception
	 */
	public static Long getGroupId(HttpServletRequest request) {
		return Long.parseLong(getCookie(request, "groupId"));
	}

	public static Integer getGroupNumber(HttpServletRequest request) {
		return Integer.parseInt(getCookie(request, "groupNumber"));
	}

	public static Long getUserId(HttpServletRequest request) {
		return Long.parseLong(getCookie(request, "userId"));
	}

	public static String getUsername(HttpServletRequest request) {
		return getCookie(request, "username");
	}

	public static String getFirstName(HttpServletRequest request) {
		return getCookie(request, "firstName");
	}

	public static String getLastName(HttpServletRequest request) {
		return getCookie(request, "lastName");
	}

	private static Pair<String, String> getEncryptedValueIVPair(String value) {
		try {
			SecretKey encryptionKey = getSecretKey();
			IvParameterSpec ivParameterSpec = EncryptionUtils.generateIv();

			return new Pair<>(EncryptionUtils.encrypt(ALGORITHM, value, encryptionKey, ivParameterSpec),
					Base64.getEncoder().encodeToString(ivParameterSpec.getIV()));
		} catch(InvalidAlgorithmParameterException | NoSuchPaddingException |
				IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException e)
		{
			System.err.println("Exception in CookieService.getEncryptedValue");
			e.printStackTrace();
		}
		return null;
	}

	private static IvParameterSpec getIv(HttpServletRequest request, String key) {
		for(Cookie cookie : request.getCookies()) {
			if(cookie.getName().equals(key + "IV")) {
				return new IvParameterSpec(Base64.getDecoder().decode(cookie.getValue().getBytes()));
			}
		}
		return null;
	}

	private static SecretKey getSecretKey() {
		byte[] decodedKey = new byte[0];
		try {
			File file = new File("file.txt");
			FileInputStream inputStream = new FileInputStream(file);

			byte[] buffer = new byte[1024];
			int bytesRead;
			StringBuilder key = new StringBuilder();

			while ((bytesRead = inputStream.read(buffer)) != -1) {
				key.append(new String(buffer, 0, bytesRead));
			}
			inputStream.close();

			decodedKey = Base64.getDecoder().decode(key.toString());
		} catch(IOException e) {
			System.err.println("Exception in getFileKey");
			e.printStackTrace();
		}
		return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
	}
}
