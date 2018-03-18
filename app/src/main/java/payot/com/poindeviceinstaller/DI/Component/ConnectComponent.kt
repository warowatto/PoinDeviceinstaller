package payot.com.poindeviceinstaller.DI.Component

import dagger.Component
import payot.com.poindeviceinstaller.Activities.ConnectActivity
import payot.com.poindeviceinstaller.DI.Module.BluetoothModule
import payot.com.poindeviceinstaller.DI.Module.NetworkModule

/**
 * Created by yongheekim on 2018. 3. 19..
 */

@Component(modules = arrayOf(NetworkModule::class, BluetoothModule::class))
interface ConnectComponent {

    fun inject(activity: ConnectActivity)
}