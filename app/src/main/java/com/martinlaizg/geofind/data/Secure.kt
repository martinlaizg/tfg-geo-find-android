package com.martinlaizg.geofind.data

import java.security.MessageDigest

object Secure {
	private val TAG = Secure::class.java.simpleName

	fun hash(toHash: String): String? {
		return hash(toHash, "SHA-512");
	}

	fun hash(toHash: String, algorithm: String): String? {
		return MessageDigest
				.getInstance(algorithm)
				.digest(toHash.toByteArray())
				.fold("", { str, it -> str + "%02x".format(it) })
	}
}