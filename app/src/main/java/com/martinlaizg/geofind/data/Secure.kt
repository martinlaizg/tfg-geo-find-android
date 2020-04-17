package com.martinlaizg.geofind.data

import java.nio.charset.StandardCharsets
import java.security.MessageDigest
import java.security.NoSuchAlgorithmException

object Secure {
    private val TAG = Secure::class.java.simpleName

    @kotlin.jvm.JvmStatic
    fun hash(toHash: String): String? {
        var hashed: String? = null
        try {
            val mdSHA512 = MessageDigest.getInstance("SHA-512")
            val bytes = mdSHA512.digest(toHash.toByteArray(StandardCharsets.UTF_8))
            val sb = StringBuilder()
            for (aByte in bytes) {
                sb.append(Integer.toString((aByte and 0xff) + 0x100, 16).substring(1))
            }
            hashed = sb.toString()
        } catch (e: NoSuchAlgorithmException) {
            e.printStackTrace()
        }
        return hashed
    }
}