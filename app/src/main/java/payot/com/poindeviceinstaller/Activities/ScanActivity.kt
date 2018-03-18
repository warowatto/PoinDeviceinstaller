package payot.com.poindeviceinstaller.Activities

import android.bluetooth.BluetoothDevice
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.widget.ArrayAdapter
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.activity_scan.*
import payot.com.poindeviceinstaller.Interface.DeviceScanner
import payot.com.poindeviceinstaller.MainActivity
import payot.com.poindeviceinstaller.Module.PoinDeviceScanner
import payot.com.poindeviceinstaller.R

/**
 * Created by yongheekim on 2018. 3. 9..
 */
class ScanActivity : AppCompatActivity() {

    lateinit var bluetoothScanner: DeviceScanner
    lateinit var adapter: ArrayAdapter<String>
    val deviceList: ArrayList<BluetoothDevice> = arrayListOf()
    val deviceNameList: ArrayList<String> = arrayListOf()

    val compositeDisposable = CompositeDisposable()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_scan)

        bluetoothScanner = PoinDeviceScanner()
        adapter = ArrayAdapter(this, android.R.layout.simple_list_item_1, deviceNameList)

        list.adapter = adapter
        list.setOnItemClickListener { adapterView, view, i, l ->
            val intent = Intent(this, MainActivity::class.java)
            val device = deviceList[i]
            intent.putExtra("device", device)

            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        deviceList.clear()
        deviceNameList.clear()
        adapter.notifyDataSetChanged()
        bluetoothScanner.scanDevice()
                .distinct()
                .subscribe(
                        {
                            deviceList.add(it)
                            deviceNameList.add(it.name)
                            adapter.notifyDataSetChanged()
                        },
                        {
                            it.printStackTrace()
                        }
                ).addTo(compositeDisposable)
    }

    override fun onPause() {
        super.onPause()
        bluetoothScanner.stopScan()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.dispose()
    }
}