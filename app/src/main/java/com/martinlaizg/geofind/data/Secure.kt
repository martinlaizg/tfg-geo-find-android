package com.martinlaizg.geofind.data

import java.security.MessageDigest

object Secure {

	fun hash(toHash: String, algorithm: String = "SHA-512"): String? {
		return MessageDigest
				.getInstance(algorithm)
				.digest(toHash.toByteArray())
				.fold("", { str, it -> str + "%02x".format(it) })
	}
}