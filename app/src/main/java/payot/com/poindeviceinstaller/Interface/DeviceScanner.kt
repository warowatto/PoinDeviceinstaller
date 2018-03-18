package payot.com.poindeviceinstaller.Interface

import android.bluetooth.BluetoothDevice
import io.reactivex.Observable

/**
 * Created by yongheekim on 2018. 3. 8..
 */
interface DeviceScanner {

    fun scanDevice(): Observable<BluetoothDevice>

    fun stopScan()
}