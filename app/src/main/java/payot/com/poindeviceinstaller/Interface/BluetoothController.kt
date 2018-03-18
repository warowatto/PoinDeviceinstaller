package payot.com.poindeviceinstaller.Interface

import android.bluetooth.BluetoothDevice
import android.content.Context
import io.reactivex.Observable

/**
 * Created by yongheekim on 2018. 3. 7..
 */
interface BluetoothController {

    fun connect(context: Context, bluetoothDevice: BluetoothDevice): Observable<Int>

    fun sendMessage(byteArray: ByteArray): Observable<ByteArray>

    fun disConnect()
}