package payot.com.poindeviceinstaller.Module

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothDevice
import io.reactivex.Observable
import payot.com.poindeviceinstaller.Interface.DeviceScanner

/**
 * Created by yongheekim on 2018. 3. 8..
 */
class PoinDeviceScanner : DeviceScanner {

    private val bluetoothAdapter = BluetoothAdapter.getDefaultAdapter()
    private lateinit var bluetoothLeScanner: BluetoothAdapter.LeScanCallback

    private val regex = "^([0-9A-Fa-f]{2}[:-]){5}([0-9A-Fa-f]{2})\$"

    override fun scanDevice(): Observable<BluetoothDevice> =
            Observable.create { emitter ->
                bluetoothLeScanner = BluetoothAdapter.LeScanCallback { device, _, _ ->
                    val deviceName = device.name
                    val deviceMac = device.address

                    if (deviceMac.replace(":", "").equals(deviceName)) {
                        emitter.onNext(device)
                    }
                }

                bluetoothAdapter.startLeScan(bluetoothLeScanner)
            }

    override fun stopScan() {
        bluetoothAdapter.stopLeScan(bluetoothLeScanner)
    }
}