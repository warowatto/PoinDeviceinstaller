package payot.com.poindeviceinstaller.RestfulAPI

/**
 * Created by yongheekim on 2018. 3. 19..
 */
data class Machine(val companyId: Int, val macAddress: String, val deviceName: String, val displayName: String, val typeId: Int, val description: String)