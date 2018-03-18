package payot.com.poindeviceinstaller.DI.Component

import dagger.Component
import payot.com.poindeviceinstaller.DI.Module.NetworkModule
import retrofit2.Retrofit

/**
 * Created by yongheekim on 2018. 3. 19..
 */

@Component(modules = arrayOf(NetworkModule::class))
interface TestComponent {
    fun retrofit(): Retrofit
}