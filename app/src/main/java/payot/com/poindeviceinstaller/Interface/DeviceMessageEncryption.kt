package payot.com.poindeviceinstaller.Interface

/**
 * Created by yongheekim on 2018. 3. 7..
 */
interface DeviceMessageEncryption {

    fun encryption(byteArray: ByteArray): ByteArray

    fun decryption(byteArray: ByteArray): ByteArray
}