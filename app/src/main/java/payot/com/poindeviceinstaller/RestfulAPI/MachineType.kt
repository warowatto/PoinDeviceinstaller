package payot.com.poindeviceinstaller.RestfulAPI

import android.os.Parcel
import android.os.Parcelable

/**
 * Created by yongheekim on 2018. 3. 19..
 */
data class MachineType(val id: Int, val name: String, val continueSell: Boolean) : Parcelable {
    constructor(source: Parcel) : this(
            source.readInt(),
            source.readString(),
            1 == source.readInt()
    )

    override fun describeContents() = 0

    override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
        writeInt(id)
        writeString(name)
        writeInt((if (continueSell) 1 else 0))
    }

    companion object {
        @JvmField
        val CREATOR: Parcelable.Creator<MachineType> = object : Parcelable.Creator<MachineType> {
            override fun createFromParcel(source: Parcel): MachineType = MachineType(source)
            override fun newArray(size: Int): Array<MachineType?> = arrayOfNulls(size)
        }
    }
}