package payot.com.poindeviceinstaller.Interface

/**
 * Created by yongheekim on 2018. 3. 7..
 */
interface DeviceMessageConverter {
    fun sendMessageConvert(string: String): ByteArray

    fun reciveMessageConvert(byteArray: ByteArray): List<String>?
}