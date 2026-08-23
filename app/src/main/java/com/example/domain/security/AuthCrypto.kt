package com.example.domain.security

import java.security.MessageDigest
import java.security.SecureRandom
import java.util.Base64
import org.json.JSONObject

object AuthCrypto {

    fun generateSalt(): String {
        val random = SecureRandom()
        val saltBytes = ByteArray(16)
        random.nextBytes(saltBytes)
        return bytesToHex(saltBytes)
    }

    fun hashPassword(password: String, salt: String): String {
        val md = MessageDigest.getInstance("SHA-256")
        val salted = "$password:$salt"
        val hashBytes = md.digest(salted.toByteArray(Charsets.UTF_8))
        return bytesToHex(hashBytes)
    }

    fun verifyPassword(password: String, salt: String, expectedHash: String): Boolean {
        val calculated = hashPassword(password, salt)
        return calculated.equals(expectedHash, ignoreCase = true)
    }

    fun createJwtToken(userId: Long, email: String, role: String): String {
        val header = JSONObject().apply {
            put("alg", "HS256")
            put("typ", "JWT")
        }.toString()

        val now = System.currentTimeMillis()
        val exp = now + (7 * 24 * 3600 * 1000L) // 7 days

        val payload = JSONObject().apply {
            put("sub", userId.toString())
            put("email", email)
            put("role", role)
            put("iat", now / 1000)
            put("exp", exp / 1000)
            put("iss", "SmartBank-AI-Auth")
        }.toString()

        val base64Header = Base64.getUrlEncoder().withoutPadding().encodeToString(header.toByteArray(Charsets.UTF_8))
        val base64Payload = Base64.getUrlEncoder().withoutPadding().encodeToString(payload.toByteArray(Charsets.UTF_8))

        // Create HMAC-SHA256 signature simulation using secret salt
        val content = "$base64Header.$base64Payload"
        val md = MessageDigest.getInstance("SHA-256")
        val sigBytes = md.digest("$content:SMART_BANK_AI_SECRET_2026".toByteArray(Charsets.UTF_8))
        val base64Signature = Base64.getUrlEncoder().withoutPadding().encodeToString(sigBytes)

        return "$base64Header.$base64Payload.$base64Signature"
    }

    fun decodeJwtPayload(token: String): JSONObject? {
        return try {
            val parts = token.split(".")
            if (parts.size != 3) return null
            val decodedBytes = Base64.getUrlDecoder().decode(parts[1])
            JSONObject(String(decodedBytes, Charsets.UTF_8))
        } catch (e: Exception) {
            null
        }
    }

    private fun bytesToHex(bytes: ByteArray): String {
        val sb = StringBuilder()
        for (b in bytes) {
            sb.append(String.format("%02x", b))
        }
        return sb.toString()
    }
}
