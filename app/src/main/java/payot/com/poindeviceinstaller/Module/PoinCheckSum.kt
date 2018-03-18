package payot.com.poindeviceinstaller.Module

import payot.com.poindeviceinstaller.Interface.DeviceCheckSum
import java.nio.ByteBuffer
import java.nio.ByteOrder

/**
 * Created by yongheekim on 2018. 3. 8..
 */
class PoinCheckSum : DeviceCheckSum {
    override fun crc(byteArray: ByteArray): ByteArray {
        var crc = 0xffff
        var flag: Int

        for (b in byteArray) {
            crc = crc xor (b.toInt() and 0xff)

            for (i in 0..7) {
                flag = crc and 1
                crc = crc shr 1
                if (flag != 0) {
                    crc = crc xor 0xa001
                }
            }
        }

        // println(crc)

        val buffer = ByteBuffer.allocate(2)
        buffer.order(ByteOrder.LITTLE_ENDIAN)

        while (buffer.hasRemaining()) {
            buffer.putShort(crc.toShort())
        }

        return buffer.array()
    }
}