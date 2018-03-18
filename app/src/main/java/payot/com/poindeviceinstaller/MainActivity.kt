package payot.com.poindeviceinstaller

import android.app.ProgressDialog
import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.Toast
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_main.*
import payot.com.poindeviceinstaller.Interface.BluetoothController
import payot.com.poindeviceinstaller.Interface.DeviceMessageConverter
import payot.com.poindeviceinstaller.Interface.DeviceMessageEncryption
import payot.com.poindeviceinstaller.Module.PoinCheckSum
import payot.com.poindeviceinstaller.Module.PoinDeviceController
import payot.com.poindeviceinstaller.Module.PoinMessageConverter
import payot.com.poindeviceinstaller.Module.PoinMessageEncryption

class MainActivity : AppCompatActivity() {

    lateinit var controller: BluetoothController
    lateinit var encryption: DeviceMessageEncryption
    lateinit var messageConverter: DeviceMessageConverter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        controller = PoinDeviceController()
        encryption = PoinMessageEncryption()
        messageConverter = PoinMessageConverter(PoinCheckSum())

        val macAddress = "D4:36:39:BC:17:A6"
        // val device = BluetoothAdapter.getDefaultAdapter().getRemoteDevice(macAddress)
        val device: BluetoothDevice = intent.extras.getParcelable("device")

        val dialog = ProgressDialog(this).apply {
            setTitle("장치와 연결 중 입니다")
            setMessage("잠시만 기다려 주십시오")
            setCancelable(false)
        }
        dialog.show()
        controller.connect(this, device)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            if (it == 1000) {
                                dialog.dismiss()
                            }
                        },
                        {
                            dialog.dismiss()
                            it.printStackTrace()
                            this.finish()
                        }
                )

        button.setOnClickListener {
            val companyNumber = edit_companyNumber.text.toString()
            val companyInfoString = "CMD S B $companyNumber"
            val companyCommend = encryption.encryption(messageConverter.sendMessageConvert(companyInfoString))

            val minimalCoin = resources.getStringArray(R.array.coin_type)[spinner_coinType.selectedItemPosition]
            val signalOffset = edit_signalOffset.text.toString()
            val signalCount = edit_signalCount.text.toString()
            val coinInfoString = "CMD S C $minimalCoin $signalOffset $signalCount"
            val coinInfocommend = encryption.encryption(messageConverter.sendMessageConvert(coinInfoString))

            val defaultProductPrice = edit_defaultPrice.text.toString()
            val minimalProductPriceInfoString = "CMD S D $defaultProductPrice"
            val minimalProductPriceInfoCommend = encryption.encryption(messageConverter.sendMessageConvert(minimalProductPriceInfoString))


            val companyUpdate = controller.sendMessage(companyCommend).map { encryption.decryption(it) }.map { messageConverter.reciveMessageConvert(it) }
                    .doOnNext { println(it) }
            val machineUpdate = controller.sendMessage(coinInfocommend).map { encryption.decryption(it) }.map { messageConverter.reciveMessageConvert(it) }
                    .doOnNext { println(it) }
            val defaultProductUpdate = controller.sendMessage(minimalProductPriceInfoCommend).map { encryption.decryption(it) }.map { messageConverter.reciveMessageConvert(it) }
                    .doOnNext { println(it) }

            val button = it
            button.isEnabled = false
            companyUpdate.flatMap { machineUpdate }.flatMap { defaultProductUpdate }
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(
                            {
                                button.isEnabled = true
                                Toast.makeText(this@MainActivity, "설정이 완료되었습니다" + it.toString(), Toast.LENGTH_SHORT).show()
                            },
                            {
                                it.printStackTrace()
                            }
                    )

        }
    }

    override fun onDestroy() {
        super.onDestroy()

        controller.disConnect()
    }
}
