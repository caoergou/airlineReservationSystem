package com.young_zy.payment.service

import java.math.BigInteger
import java.security.SecureRandom
import javax.crypto.SecretKeyFactory
import javax.crypto.spec.PBEKeySpec

object PasswordHash {
    private const val PBKDF2_ALGORITHM = "PBKDF2WithHmacSHA256"
    // The following constants may be changed without breaking existing hashes.
    private const val SALT_BYTE_SIZE = 24
    private const val HASH_BYTE_SIZE = 24
    private const val PBKDF2_ITERATIONS = 1000
    private const val ITERATION_INDEX = 0
    private const val SALT_INDEX = 1
    private const val PBKDF2_INDEX = 2
    /**
     * Returns a salted PBKDF2 hash of the password.
     *
     * @param password the password to hash
     * @return a salted PBKDF2 hash of the password
     */
    fun createHash(password: String): String {
        return createHash(password.toCharArray())
    }

    /**
     * Returns a salted PBKDF2 hash of the password.
     *
     * @param password the password to hash
     * @return a salted PBKDF2 hash of the password
     */
    fun createHash(password: CharArray): String { // Generate a random salt
        val random = SecureRandom()
        val salt = ByteArray(SALT_BYTE_SIZE)
        random.nextBytes(salt)
        // Hash the password
        val hash = pbkdf2(password, salt, PBKDF2_ITERATIONS, HASH_BYTE_SIZE)
        // format iterations:salt:hash
        return PBKDF2_ITERATIONS.toString() + ":" + toHex(salt) + ":" + toHex(hash)
    }

    /**
     * Validates a password using a hash.
     *
     * @param password    the password to check
     * @param correctHash the hash of the valid password
     * @return true if the password is correct, false if not
     */
    fun validatePassword(password: String, correctHash: String): Boolean {
        return validatePassword(password.toCharArray(), correctHash)
    }

    /**
     * Validates a password using a hash.
     *
     * @param password    the password to check
     * @param correctHash the hash of the valid password
     * @return true if the password is correct, false if not
     */
    fun validatePassword(password: CharArray, correctHash: String): Boolean { // Decode the hash into its parameters
        val params = correctHash.split(":").toTypedArray()
        val iterations = params[ITERATION_INDEX].toInt()
        val salt = fromHex(params[SALT_INDEX])
        val hash = fromHex(params[PBKDF2_INDEX])
        // Compute the hash of the provided password, using the same salt,
        // iteration count, and hash length
        val testHash = pbkdf2(password, salt, iterations, hash.size)
        // Compare the hashes in constant time. The password is correct if
        // both hashes match.
        return slowEquals(hash, testHash)
    }

    /**
     * Compares two byte arrays in length-constant time. This comparison method
     * is used so that password hashes cannot be extracted from an on-line
     * system using a timing attack and then attacked off-line.
     *
     * @param a the first byte array
     * @param b the second byte array
     * @return true if both byte arrays are the same, false if not
     */
    private fun slowEquals(a: ByteArray, b: ByteArray): Boolean {
        var diff = a.size xor b.size
        var i = 0
        while (i < a.size && i < b.size) {
            diff = diff or a[i].toInt() xor b[i].toInt()
            i++
        }
        return diff == 0
    }

    /**
     * Computes the PBKDF2 hash of a password.
     *
     * @param password   the password to hash.
     * @param salt       the salt
     * @param iterations the iteration count (slowness factor)
     * @param bytes      the length of the hash to compute in bytes
     * @return the PBDKF2 hash of the password
     */
//    @Throws(NoSuchAlgorithmException::class, InvalidKeySpecException::class)
    private fun pbkdf2(password: CharArray, salt: ByteArray, iterations: Int, bytes: Int): ByteArray {
        val spec = PBEKeySpec(password, salt, iterations, bytes * 8)
        val skf = SecretKeyFactory.getInstance(PBKDF2_ALGORITHM)
        return skf.generateSecret(spec).encoded
    }

    /**
     * Converts a string of hexadecimal characters into a byte array.
     *
     * @param hex the hex string
     * @return the hex string decoded into a byte array
     */
    private fun fromHex(hex: String): ByteArray {
        val binary = ByteArray(hex.length / 2)
        for (i in binary.indices) {
            binary[i] = hex.substring(2 * i, 2 * i + 2).toInt(16).toByte()
        }
        return binary
    }

    /**
     * Converts a byte array into a hexadecimal string.
     *
     * @param array the byte array to convert
     * @return a length*2 character string encoding the byte array
     */
    private fun toHex(array: ByteArray): String {
        val bi = BigInteger(1, array)
        val hex = bi.toString(16)
        val paddingLength = array.size * 2 - hex.length
        return if (paddingLength > 0) String.format("%0" + paddingLength + "d", 0) + hex else hex
    }
}