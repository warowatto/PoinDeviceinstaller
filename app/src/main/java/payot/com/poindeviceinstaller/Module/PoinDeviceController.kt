package payot.com.poindeviceinstaller.Module

import android.bluetooth.*
import android.content.Context
import android.support.annotation.IntegerRes
import io.reactivex.Observable
import io.reactivex.rxkotlin.toObservable
import io.reactivex.rxkotlin.zipWith
import io.reactivex.schedulers.Schedulers
import io.reactivex.subjects.PublishSubject
import payot.com.poindeviceinstaller.Interface.BluetoothController
import java.util.concurrent.TimeUnit

/**
 * Created by yongheekim on 2018. 3. 7..
 */
class PoinDeviceController : BluetoothGattCallback(), BluetoothController {

    private lateinit var gatt: BluetoothGatt
    private lateinit var char: BluetoothGattCharacteristic

    private val statusMessage = PublishSubject.create<Int>()
    private val responseMessage = PublishSubject.create<Byte>()
    private val response = responseMessage.buffer(64).map { it.toByteArray() }

    override fun connect(context: Context, bluetoothDevice: BluetoothDevice): Observable<Int> {
        bluetoothDevice.connectGatt(context, false, this@PoinDeviceController)

        return statusMessage
    }

    override fun sendMessage(byteArray: ByteArray): Observable<ByteArray> {
        val messageSlice = byteArray.toObservable().buffer(20).map { it.toByteArray() }
        val messageDelay = Observable.interval(0, 50L, TimeUnit.MILLISECONDS, Schedulers.computation())

        return messageSlice.zipWith(messageDelay, { message: ByteArray, _: Long ->
            return@zipWith message
        }).doOnNext {
            char.value = it
            gatt.writeCharacteristic(char)
        }.takeLast(1).flatMap { response }.take(1)
    }

    override fun disConnect() {
        this.gatt.disconnect()
    }

    override fun onConnectionStateChange(gatt: BluetoothGatt?, status: Int, newState: Int) {
        super.onConnectionStateChange(gatt, status, newState)

        statusMessage.onNext(newState)

        when (newState) {
            BluetoothProfile.STATE_CONNECTED -> {
                this.gatt = gatt!!
                gatt.discoverServices()

            }
        }
    }

    override fun onServicesDiscovered(gatt: BluetoothGatt?, status: Int) {
        super.onServicesDiscovered(gatt, status)
        val findService = findChar(this.gatt)

        this.char = findService?.second!!
        if (findService != null && this.gatt.setCharacteristicNotification(findService.second, true)) {
            statusMessage.onNext(1000)
        }
    }

    override fun onCharacteristicChanged(gatt: BluetoothGatt?, characteristic: BluetoothGattCharacteristic?) {
        super.onCharacteristicChanged(gatt, characteristic)
        characteristic?.let {
            val message = it.value
            message.forEach { responseMessage.onNext(it) }
        }
    }

    fun findChar(bluetoothGatt: BluetoothGatt): Pair<BluetoothGattService, BluetoothGattCharacteristic>? {
        for (gattService in bluetoothGatt.services) {
            for (gattChar in gattService.characteristics) {
                if (isWriteable(gattChar) && isNotify(gattChar)) {
                    return gattService to gattChar
                }
            }
        }

        return null
    }

    fun isWriteable(characteristic: BluetoothGattCharacteristic): Boolean {
        return (characteristic.properties and (BluetoothGattCharacteristic.PROPERTY_WRITE or BluetoothGattCharacteristic.PROPERTY_WRITE_NO_RESPONSE)) != 0
    }

    fun isNotify(characteristic: BluetoothGattCharacteristic): Boolean {
        return (characteristic.properties and (BluetoothGattCharacteristic.PROPERTY_NOTIFY) != 0)
    }
}