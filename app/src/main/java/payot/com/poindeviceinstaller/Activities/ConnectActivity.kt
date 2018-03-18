package payot.com.poindeviceinstaller.Activities

import android.app.AlertDialog
import android.app.ProgressDialog
import android.bluetooth.BluetoothDevice
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import payot.com.poindeviceinstaller.App
import payot.com.poindeviceinstaller.DI.Component.DaggerConnectComponent
import payot.com.poindeviceinstaller.DI.Module.BluetoothModule
import payot.com.poindeviceinstaller.DI.Module.NetworkModule
import payot.com.poindeviceinstaller.Interface.BluetoothController
import payot.com.poindeviceinstaller.Interface.DeviceMessageConverter
import payot.com.poindeviceinstaller.R
import payot.com.poindeviceinstaller.RestfulAPI.Machine
import payot.com.poindeviceinstaller.RestfulAPI.MachineInstallAPI
import javax.inject.Inject

/**
 * Created by yongheekim on 2018. 3. 19..
 */
class ConnectActivity : AppCompatActivity() {

    @Inject
    lateinit var bluetoothController: BluetoothController

    @Inject
    lateinit var converter: DeviceMessageConverter

    @Inject
    lateinit var api: MachineInstallAPI

    val dispose: CompositeDisposable = CompositeDisposable()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_connect)

        DaggerConnectComponent.builder()
                .bluetoothModule(BluetoothModule())
                .networkModule(NetworkModule())
                .build().inject(this)

        val device = intent.getParcelableExtra<BluetoothDevice>("device")

        val progress = ProgressDialog(this).apply {
            setTitle("접속중 입니다")
            setMessage("잠시만 기다려 주세요")
            setCancelable(false)
        }

        progress.show()
        bluetoothController.connect(this, device)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            when (it) {
                                1000 -> progress.dismiss()
                            }
                        },
                        {
                            it.printStackTrace()
                        }
                ).addTo(dispose)

    }

    fun upload(name: String, displayName: String, typeId: Int, coin: Int, offset: Int, times: Int, description: String = "") {

        val device = intent.getParcelableExtra<BluetoothDevice>("device")
        val machine = Machine(App.company.id, device.address, name, displayName, typeId, description)

        val message1 = "CMD S A ON"
        val message2 = "CMD S B ${App.company.comapnyNumber}"
        val message3 = "CMD S C $coin $offset $times"

        val progressDialog = ProgressDialog(this).apply {
            setTitle("설정 중")
            setMessage("장치를 설정중입니다.\n접속을 종료하시지 마세요")
            setCancelable(false)
        }

        progressDialog.show()

        bluetoothController.sendMessage(converter.sendMessageConvert(message1))
                .flatMap { bluetoothController.sendMessage(converter.sendMessageConvert(message2)) }
                .flatMap { bluetoothController.sendMessage(converter.sendMessageConvert(message3)) }
                .flatMap { api.installMachine(machine).toObservable() }
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        {
                            progressDialog.dismiss()
                            AlertDialog.Builder(this)
                                    .setTitle("등록 성공")
                                    .setMessage("성공적으로 장치가 등록되었습니다")
                                    .setNeutralButton("닫기") { _, _ -> }
                                    .setOnDismissListener { this.finish() }
                                    .show()
                        },
                        {
                            progressDialog.dismiss()
                            it.printStackTrace()
                        }
                )

    }

    override fun onDestroy() {
        super.onDestroy()
        dispose.dispose()
    }
}