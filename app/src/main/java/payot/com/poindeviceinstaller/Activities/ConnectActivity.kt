package payot.com.poindeviceinstaller.Activities

import android.support.v7.app.AppCompatActivity
import payot.com.poindeviceinstaller.Interface.BluetoothController
import javax.inject.Inject

/**
 * Created by yongheekim on 2018. 3. 19..
 */
class ConnectActivity : AppCompatActivity() {

    @Inject
    lateinit var bluetoothController: BluetoothController
}