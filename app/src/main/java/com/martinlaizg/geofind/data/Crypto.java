package com.martinlaizg.geofind.data;

import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Crypto {

	private static MessageDigest mdSHA512;
	private static String TAG = Crypto.class.getSimpleName();

	public static String hash(String toHash) {
		String hashed = null;
		try {
			if(mdSHA512 == null) {
				mdSHA512 = MessageDigest.getInstance("SHA-512");
			}
			// mdSHA512.update(salt);
			byte[] bytes = mdSHA512.digest(toHash.getBytes());
			StringBuilder sb = new StringBuilder();
			for(int i = 0; i < bytes.length; i++) {
				sb.append(Integer.toString((bytes[i] & 0xff) + 0x100, 16).substring(1));
			}
			hashed = sb.toString();
		} catch(NoSuchAlgorithmException e) {
			e.printStackTrace();
		}
		Log.d(TAG, "hash: " + hashed);
		return hashed;

	}
}
