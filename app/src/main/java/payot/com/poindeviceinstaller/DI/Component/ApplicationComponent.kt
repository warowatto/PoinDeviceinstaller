package payot.com.poindeviceinstaller.DI.Component

import dagger.Component
import payot.com.poindeviceinstaller.App
import payot.com.poindeviceinstaller.DI.Module.NetworkModule
import javax.inject.Singleton

/**
 * Created by yongheekim on 2018. 3. 9..
 */

@Singleton
@Component(modules = arrayOf(NetworkModule::class))
interface ApplicationComponent {
    fun inject(app: App)
}