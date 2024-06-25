package com.example.zrwenxue

fun encrypt(plaintext: String, key: String): String {
    val charArray = plaintext.toCharArray()
    for (i in charArray.indices) {
        charArray[i] = (charArray[i].toInt() xor key[i % key.length].toInt()).toChar()
    }
    return String(charArray)
}

fun decrypt(ciphertext: String, key: String): String {
    return encrypt(ciphertext, key)
}


fun main(){
    val plaintext = "这辈子非张娟不娶"+System.currentTimeMillis()+"我是密码"+"我是持有人"
    val key = "123456"

    val ciphertext = encrypt(plaintext, key)//加密
    println("Encrypted: $ciphertext")

    val decryptedText = decrypt(ciphertext, key)//解密
    println("Decrypted: $decryptedText")
}