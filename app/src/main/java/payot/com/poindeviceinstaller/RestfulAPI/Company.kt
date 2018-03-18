package payot.com.poindeviceinstaller.RestfulAPI

import android.os.Parcel
import android.os.Parcelable
import java.util.*

/**
 * Created by yongheekim on 2018. 3. 10..
 */
data class Company(
        val id: Int,
        val comapnyNumber: String,
        val email: String,
        val name: String,
        var tel: String?,
        var phone: String?,
        var fax: String?,
        var create_at: Date) : Parcelable {
    constructor(source: Parcel) : this(
            source.readInt(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readString(),
            source.readSerializable() as Date
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(id)
        writeString(comapnyNumber)
        writeString(email)
        writeString(name)
        writeString(tel)
        writeString(phone)
        writeString(fax)
        writeSerializable(create_at)
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<Company> = object : Parcelable.Creator<Company> {
            override fun createFromParcel(source: Parcel): Company = Company(source)
            override fun newArray(size: Int): Array<Company?> = arrayOfNulls(size)
        }
    }
}