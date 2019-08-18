package com.martinlaizg.geofind.data;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Secure {

	private static final String TAG = Secure.class.getSimpleName();

	public static String hash(String toHash) {
		String hashed = null;
		try {
			MessageDigest mdSHA512 = MessageDigest.getInstance("SHA-512");
			byte[] bytes = mdSHA512.digest(toHash.getBytes(StandardCharsets.UTF_8));
			StringBuilder sb = new StringBuilder();
			for(byte aByte : bytes) {
				sb.append(Integer.toString((aByte & 0xff) + 0x100, 16).substring(1));
			}
			hashed = sb.toString();
		} catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		return hashed;
	}
}
