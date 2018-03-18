package payot.com.poindeviceinstaller.DI.Module

import com.google.gson.GsonBuilder
import dagger.Module
import dagger.Provides
import io.reactivex.schedulers.Schedulers
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import payot.com.poindeviceinstaller.RestfulAPI.MachineInstallAPI
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import java.text.DateFormat

/**
 * Created by yongheekim on 2018. 3. 9..
 */

@Module
class NetworkModule {

    private val HOST = "http://admin.payot-poin.com/"

    @Provides
    fun okhttp(): OkHttpClient =
            OkHttpClient.Builder()
                    .addNetworkInterceptor(HttpLoggingInterceptor().apply {
                        level = HttpLoggingInterceptor.Level.BASIC
                    })
                    .build()

    @Provides
    fun retrofit(okHttpClient: OkHttpClient): Retrofit =
            Retrofit.Builder()
                    .baseUrl(HOST)
                    .client(okHttpClient)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                    .addConverterFactory(GsonConverterFactory.create(GsonBuilder().setDateFormat(DateFormat.FULL).create()))
                    .build()

    @Provides
    fun api(retrofit: Retrofit): MachineInstallAPI = retrofit.create(MachineInstallAPI::class.java)
}