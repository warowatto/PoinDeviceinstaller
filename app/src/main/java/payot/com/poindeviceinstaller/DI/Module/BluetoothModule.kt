package payot.com.poindeviceinstaller.DI.Module

import dagger.Module
import dagger.Provides
import payot.com.poindeviceinstaller.Interface.BluetoothController
import payot.com.poindeviceinstaller.Interface.DeviceCheckSum
import payot.com.poindeviceinstaller.Interface.DeviceMessageConverter
import payot.com.poindeviceinstaller.Interface.DeviceMessageEncryption
import payot.com.poindeviceinstaller.Module.PoinCheckSum
import payot.com.poindeviceinstaller.Module.PoinDeviceController
import payot.com.poindeviceinstaller.Module.PoinMessageConverter
import payot.com.poindeviceinstaller.Module.PoinMessageEncryption

/**
 * Created by yongheekim on 2018. 3. 19..
 */

@Module
class BluetoothModule {

    @Provides
    fun controller(): BluetoothController = PoinDeviceController()

    @Provides
    fun messageCreater(poinCheckSum: DeviceCheckSum, messageEncryption: DeviceMessageEncryption): DeviceMessageConverter = object : DeviceMessageConverter {
        val messageConverter = PoinMessageConverter(poinCheckSum)

        override fun sendMessageConvert(string: String): ByteArray {
            val convert = messageConverter.sendMessageConvert(string)
            val hash = messageEncryption.encryption(convert)

            return hash
        }

        override fun reciveMessageConvert(byteArray: ByteArray): List<String>? {
            val plainText = messageEncryption.decryption(byteArray)
            return messageConverter.reciveMessageConvert(plainText)
        }
    }

    @Provides
    fun encrypt(): DeviceMessageEncryption = PoinMessageEncryption()

    @Provides
    fun checksum(): DeviceCheckSum = PoinCheckSum()

}