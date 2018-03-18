package payot.com.poindeviceinstaller.Interface

/**
 * Created by yongheekim on 2018. 3. 7..
 */
interface DeviceMessageSchema {

    fun sendMessage(byteArray: ByteArray): ByteArray

    fun reciveMessage(byteArray: ByteArray): ByteArray
}