package com.example.groupqueue.services;

import com.example.groupqueue.encryption.Encryption;
import com.example.groupqueue.models.dto.Pair;
import com.example.groupqueue.models.dto.User;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
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
public class CookieService {
	private static final String ALGORITHM = "AES/CBC/PKCS5Padding";
	private final UserService userService;
	private final GroupService groupService;

	@Autowired
	public CookieService(UserService userService, GroupService groupService) {
		this.userService = userService;
		this.groupService = groupService;
	}



	public static Pair<String, String> getEncryptedValueIVPair(String value) {
		try {
			SecretKey encryptionKey = getSecretKey();
			IvParameterSpec ivParameterSpec = Encryption.generateIv();

			return new Pair<>(Encryption.encrypt(ALGORITHM, value, encryptionKey, ivParameterSpec),
							Base64.getEncoder().encodeToString(ivParameterSpec.getIV()));
		} catch(InvalidAlgorithmParameterException | NoSuchPaddingException |
				IllegalBlockSizeException | NoSuchAlgorithmException | BadPaddingException | InvalidKeyException e)
		{
			System.err.println("Exception in CookieService.getEncryptedValue");
			e.printStackTrace();
		}
		return null;
	}

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

	public static boolean createCookie(HttpServletResponse response, String key, String value)	{
		Pair<String, String> encryptedValueIVPair = getEncryptedValueIVPair(value);

		String encryptedValue = encryptedValueIVPair.getFirst();
		if(encryptedValue == null) {
			return false;
		}

		Cookie cookie = new Cookie(key, encryptedValue);
		cookie.setMaxAge(3600);
		cookie.setSecure(true);
		cookie.setHttpOnly(true);
		response.addCookie(cookie);

		Cookie cookieIV = new Cookie(key + "IV", encryptedValueIVPair.getSecond());
		cookieIV.setMaxAge(3600);
		cookieIV.setSecure(true);
		cookieIV.setHttpOnly(true);
		response.addCookie(cookie);
		response.addCookie(cookieIV);

		return true;
	}

	public static String getCookie(HttpServletRequest request, String key) {
		Cookie[] cookies = request.getCookies();
		if(cookies == null) {
			return null;
		}

		for(Cookie cookie : cookies) {
			String cookieName = cookie.getName();
			if(cookieName.equals(key)) {
				return Encryption.decrypt(ALGORITHM, cookie.getValue(), getSecretKey(), getIv(request, key));
			}
		}
		return null;
	}

	public static IvParameterSpec getIv(HttpServletRequest request, String key) {
		for(Cookie cookie : request.getCookies()) {
			if(cookie.getName().equals(key + "IV")) {
				return new IvParameterSpec(Base64.getDecoder().decode(cookie.getValue().getBytes()));
			}
		}
		return null;
	}

	public static SecretKey getSecretKey() {
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

	public boolean isAddRequiredCookiesSuccess(HttpServletResponse response, User user) {
		Long userId = userService.getUserIdByUsername(user.getUsername());

		return CookieService.createCookie(response, "userId", userId.toString()) &&
				CookieService.createCookie(response, "groupId",
						groupService.getGroupIdByUserId(userId).toString());
	}

	/**
	 * method returns groupId from cookie if it exists, if does not -> null
	 */
	public static Long getGroupIdFromCookie(HttpServletRequest request) {
		Long groupId = null;
		try {
			groupId = Long.parseLong(getCookie(request, "groupId"));
		} catch(NullPointerException | NumberFormatException e) {
			System.err.println("groupId is probably NULL!");
			e.printStackTrace();
			return null;
		}
		return groupId;
	}
}
