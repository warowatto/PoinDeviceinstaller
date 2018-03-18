package payot.com.poindeviceinstaller.Module

import payot.com.poindeviceinstaller.Interface.DeviceMessageEncryption
import javax.crypto.Cipher
import javax.crypto.spec.IvParameterSpec
import javax.crypto.spec.SecretKeySpec

/**
 * Created by yongheekim on 2018. 3. 8..
 */
class PoinMessageEncryption : DeviceMessageEncryption {

    private val key = byteArrayOf(
            0x2B, 0x7E, 0x15, 0x16, 0x28,
            0xAE.toByte(), 0xD2.toByte(), 0xA6.toByte(), 0xAB.toByte(), 0xF7.toByte(),
            0x15, 0x88.toByte(), 0x09, 0xCF.toByte(), 0x4F,
            0x3C)

    private val iv = byteArrayOf(
            0x00, 0x01, 0x02, 0x03, 0x04,
            0x05, 0x06, 0x07, 0x08, 0x09,
            0x0A, 0x0B, 0x0C, 0x0D, 0x0E,
            0x0F)

    private val cipher: Cipher = Cipher.getInstance("AES/CBC/NoPadding")
    private val keySpec = SecretKeySpec(key, "AES")
    private val ivSpec = IvParameterSpec(iv)

    // CRC까지 컨텐츠 내용으로 입력받음
    override fun encryption(byteArray: ByteArray): ByteArray {
        cipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec)

        return cipher.doFinal(byteArray)
    }

    override fun decryption(byteArray: ByteArray): ByteArray {
        cipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec)

        return cipher.doFinal(byteArray)
    }

}