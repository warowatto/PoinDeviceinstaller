package payot.com.poindeviceinstaller.DI.Component

import dagger.Component
import payot.com.poindeviceinstaller.Activities.LoginActivity
import payot.com.poindeviceinstaller.DI.Module.NetworkModule

/**
 * Created by yongheekim on 2018. 3. 19..
 */

@Component(modules = arrayOf(NetworkModule::class))
interface LoginComponent {
    fun inject(activity: LoginActivity)
}