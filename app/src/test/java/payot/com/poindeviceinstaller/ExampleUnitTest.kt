package payot.com.poindeviceinstaller

import org.junit.Before
import org.junit.Test
import payot.com.poindeviceinstaller.DI.Component.DaggerTestComponent
import payot.com.poindeviceinstaller.DI.Module.NetworkModule
import payot.com.poindeviceinstaller.Module.PoinMessageConverter
import payot.com.poindeviceinstaller.RestfulAPI.MachineInstallAPI

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * See [testing documentation](http://d.android.com/tools/testing).
 */
class ExampleUnitTest {

    lateinit var api: MachineInstallAPI

    private val regex = "/^([0-9a-F]{1,2}[\\.:-]){5}([0-9a-F]{1,2})\$/"

    @Before
    fun init() {

        api = DaggerTestComponent.builder()
                .networkModule(NetworkModule())
                .build().retrofit().create(MachineInstallAPI::class.java)
    }

    val testBytes = byteArrayOf(0x00, 0x01, 0x03, 0x04)

    @Test
    fun addition_isCorrect() {

//        api.login("test@test.com", "pass")
//                .subscribe(
//                        { println(it) },
//                        { it.printStackTrace() }
//                )

        api.getMachineTypes()
                .subscribe(
                        { println(it) },
                        { it.printStackTrace() }
                )
    }

}
