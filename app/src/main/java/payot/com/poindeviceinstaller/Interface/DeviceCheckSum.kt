package payot.com.poindeviceinstaller.Interface

/**
 * Created by yongheekim on 2018. 3. 8..
 */
interface DeviceCheckSum {

    fun crc(byteArray: ByteArray): ByteArray
}