package com.martinlaizg.geofind.data;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Secure {

	private static final String TAG = Secure.class.getSimpleName();
	private static MessageDigest mdSHA512;

	public static String hash(String toHash) {
		String hashed = null;
		try {
			if(mdSHA512 == null) {
				mdSHA512 = MessageDigest.getInstance("SHA-512");
			}
			byte[] bytes = mdSHA512.digest(toHash.getBytes());
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
