package com.project.groupqueue.utils;

import com.project.groupqueue.exceptions.CookieException;
import com.project.groupqueue.models.dto.Pair;
import com.project.groupqueue.models.dto.Student;
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
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

@Service
@RequiredArgsConstructor
public class CookieUtil {
	private static final String SECRET_KEY = "b6y7UPSCt2HnClqdhikmag==";
	private static final String ALGORITHM = "AES/CBC/PKCS5Padding";

	public static boolean isCookiesExists(HttpServletRequest request) {
		return request.getCookies() != null;
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
		cookie.setHttpOnly(true);
		cookie.setPath("/");
		response.addCookie(cookie);

		Cookie cookieIV = new Cookie(key + "IV", encryptedValueIVPair.getSecond());
		cookieIV.setMaxAge(3600);
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
				return EncryptionUtil.decrypt(ALGORITHM, cookie.getValue(), getSecretKey(), getIv(request, key));
			}
		}
		throw new CookieException("there is no " + key + " cookie");
	}

	public static void addRequired(HttpServletResponse response, Student student) {
		create(response, "studentId", student.getStudentId().toString());
		create(response, "groupId", student.getGroupId().toString());
	}

	public static Long getGroupId(HttpServletRequest request) {
		return Long.parseLong(getCookie(request, "groupId"));
	}

	public static Long getStudentId(HttpServletRequest request) {
		return Long.parseLong(getCookie(request, "studentId"));
	}

	private static Pair<String, String> getEncryptedValueIVPair(String value) {
		try {
			SecretKey encryptionKey = getSecretKey();
			IvParameterSpec ivParameterSpec = EncryptionUtil.generateIv();

			return new Pair<>(EncryptionUtil.encrypt(ALGORITHM, value, encryptionKey, ivParameterSpec),
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
		byte[] decodedKey = Base64.getDecoder().decode(SECRET_KEY);
		return new SecretKeySpec(decodedKey, 0, decodedKey.length, "AES");
	}
}