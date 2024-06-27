package com.example.zrprint

import javax.crypto.Cipher
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.DESKeySpec

fun encryptDES(plaintext: String, key: String): String {
    val keyBytes = key.toByteArray(Charsets.UTF_8)
    val keySpec = DESKeySpec(keyBytes)
    val secretKeyFactory = SecretKeyFactory.getInstance("DES")
    val secretKey = secretKeyFactory.generateSecret(keySpec)

    val cipher = Cipher.getInstance("DES/ECB/PKCS5Padding")
    cipher.init(Cipher.ENCRYPT_MODE, secretKey)
    val encryptedBytes = cipher.doFinal(plaintext.toByteArray(Charsets.UTF_8))
    return encryptedBytes.toHexString()
}

fun decryptDES(ciphertext: String, key: String): String {
    val keyBytes = key.toByteArray(Charsets.UTF_8)
    val keySpec = DESKeySpec(keyBytes)
    val secretKeyFactory = SecretKeyFactory.getInstance("DES")
    val secretKey = secretKeyFactory.generateSecret(keySpec)

    val cipher = Cipher.getInstance("DES/ECB/PKCS5Padding")
    cipher.init(Cipher.DECRYPT_MODE, secretKey)
    val decryptedBytes = cipher.doFinal(ciphertext.hexStringToByteArray())
    return decryptedBytes.toString(Charsets.UTF_8)
}

private fun ByteArray.toHexString() = joinToString("") { "%02x".format(it) }
private fun String.hexStringToByteArray() = chunked(2).map { it.toInt(16).toByte() }.toByteArray()

fun main(){

    //println(encryptDES("我爱张娟12456457894王琦王琦wangqi166164619610000","wangqi1661646196"))

    println(decryptDES("5743cf9798ca74eb09e7e44493447f7f487bb994b3595a3154f0a2f88d30d38ccf84726b1ae83cc71b439fa91dbdf62683f21630b78b5770720a2d77ebac58ac","wangqi1661646196"))

}