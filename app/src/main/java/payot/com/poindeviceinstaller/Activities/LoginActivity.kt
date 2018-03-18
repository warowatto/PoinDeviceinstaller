package payot.com.poindeviceinstaller.Activities

import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import kotlinx.android.synthetic.main.activity_login.*
import payot.com.poindeviceinstaller.App
import payot.com.poindeviceinstaller.DI.Component.DaggerLoginComponent
import payot.com.poindeviceinstaller.DI.Module.NetworkModule
import payot.com.poindeviceinstaller.R
import payot.com.poindeviceinstaller.RestfulAPI.MachineInstallAPI
import javax.inject.Inject

/**
 * Created by yongheekim on 2018. 3. 9..
 */
class LoginActivity : AppCompatActivity() {

    @Inject
    lateinit var api: MachineInstallAPI

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        supportActionBar?.hide()

        DaggerLoginComponent.builder().networkModule(NetworkModule())
                .build().inject(this)

        btnLogin.setOnClickListener {
            val email = editEmail.text.toString()
            val password = editPassword.text.toString()

            val progressDialog = ProgressDialog(this).apply {
                setMessage("로그인 중 입니다")
            }

            progressDialog.show()
            api.login(email, password)
                    .doOnEvent { _, _ -> progressDialog.dismiss() }
                    .subscribe(
                            {
                                App.company = it
                                startActivity(Intent(this, ScanActivity::class.java))
                            },
                            {
                                it.printStackTrace()
                            })
        }
    }
}