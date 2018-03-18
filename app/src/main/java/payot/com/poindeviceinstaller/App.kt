package payot.com.poindeviceinstaller

import android.app.Application
import payot.com.poindeviceinstaller.RestfulAPI.Company

/**
 * Created by yongheekim on 2018. 3. 10..
 */
class App : Application() {

    companion object {
        lateinit var company: Company
    }

    override fun onCreate() {
        super.onCreate()


    }
}