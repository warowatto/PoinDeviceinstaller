package payot.com.poindeviceinstaller.RestfulAPI

import io.reactivex.Single
import retrofit2.http.*

/**
 * Created by yongheekim on 2018. 3. 9..
 */
interface MachineInstallAPI {

    @FormUrlEncoded
    @POST("/api/install/user")
    fun login(
            @Field("email") email: String,
            @Field("password") password: String): Single<Company>

    @GET("/api/types")
    fun getMachineTypes(): Single<List<MachineType>>

    @FormUrlEncoded
    @POST("/api/machine")
    fun installMachine(@Body machine: Machine): Single<Map<String, String>>
}