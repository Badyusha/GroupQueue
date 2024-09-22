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
		Pair<String, String> encryptedValueIVPair = EncryptionUtil.getEncryptedValueIVPair(value);
		if(encryptedValueIVPair == null) {
			throw new CookieException("cannot create cookie");
		}

		String encryptedValue = encryptedValueIVPair.getFirst();
		Cookie cookie = new Cookie(key, encryptedValue);
		fillInCookie(cookie);
		response.addCookie(cookie);

		String encryptedIV = encryptedValueIVPair.getSecond();
		Cookie cookieIV = new Cookie(key + "IV", encryptedIV);
		fillInCookie(cookieIV);
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
				return EncryptionUtil.decrypt(cookie.getValue(), EncryptionUtil.getIv(request, key));
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

	private static void fillInCookie(Cookie cookie) {
		cookie.setMaxAge(3600);
		cookie.setHttpOnly(true);
		cookie.setPath("/");
	}
}