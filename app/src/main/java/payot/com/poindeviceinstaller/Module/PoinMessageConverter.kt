package payot.com.poindeviceinstaller.Module

import payot.com.poindeviceinstaller.Interface.DeviceCheckSum
import payot.com.poindeviceinstaller.Interface.DeviceMessageConverter
import java.security.SecureRandom

/**
 * Created by yongheekim on 2018. 3. 8..
 */
class PoinMessageConverter(val deviceCheckSum: DeviceCheckSum) : DeviceMessageConverter {

    override fun sendMessageConvert(string: String): ByteArray {
        val content = string.toByteArray()

        val totalMessageSize = 64
        val firstRandomSize = 4
        val contentSize = content.size
        val checksum = deviceCheckSum.crc(content)
        val lastRandomSize = totalMessageSize - firstRandomSize - contentSize - checksum.size

        val randomByte = SecureRandom()
        return byteArrayOf(
                *randomByte.generateSeed(firstRandomSize),
                *content,
                *checksum,
                *randomByte.generateSeed(lastRandomSize))
    }


    override fun reciveMessageConvert(byteArray: ByteArray): List<String>? {
        val message = byteArray.drop(4)
        val validationCrcValue = byteArrayOf(0x00, 0x00)

        var resultBytes: ByteArray = byteArrayOf()

        checksum@
        for ((index, _) in message.withIndex()) {
            val nowMessage = message.take(index + 1).toByteArray()
            if (deviceCheckSum.crc(nowMessage) contentEquals validationCrcValue) {
                resultBytes = nowMessage.dropLast(2).toByteArray()
                break@checksum
            }
        }

        return resultBytes.toString(Charsets.UTF_8).split(" ")

    }

}