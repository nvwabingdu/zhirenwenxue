package com.example.zrprint

import javax.crypto.Cipher
import javax.crypto.spec.SecretKeySpec

fun encryptAES(plaintext: String, key: String): String {
    val keyBytes = key.toByteArray(Charsets.UTF_8)
    val secretKey = SecretKeySpec(keyBytes, "AES")

    val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
    cipher.init(Cipher.ENCRYPT_MODE, secretKey)
    val encryptedBytes = cipher.doFinal(plaintext.toByteArray(Charsets.UTF_8))
    return encryptedBytes.toHexString()
}

fun decryptAES(ciphertext: String, key: String): String {
    val keyBytes = key.toByteArray(Charsets.UTF_8)
    val secretKey = SecretKeySpec(keyBytes, "AES")

    val cipher = Cipher.getInstance("AES/ECB/PKCS5Padding")
    cipher.init(Cipher.DECRYPT_MODE, secretKey)
    val decryptedBytes = cipher.doFinal(ciphertext.hexStringToByteArray())
    return decryptedBytes.toString(Charsets.UTF_8)
}

private fun ByteArray.toHexString() = joinToString("") { "%02x".format(it) }
private fun String.hexStringToByteArray() = chunked(2).map { it.toInt(16).toByte() }.toByteArray()


fun main(){

    println(encryptAES("我爱张娟|1719275156044|瓦格|wangqi166|0","d2266186deeeb70f4b0b656bb22ca484"))

    println(decryptAES("ce4a7e9eaa63d5a3a1c62b7665c37a3f210e67639265e0482e5cf9733dcbdba1997854aa9aa4fce1dba6f12ba7830411","d2266186deeeb70f4b0b656bb22ca484"))

}