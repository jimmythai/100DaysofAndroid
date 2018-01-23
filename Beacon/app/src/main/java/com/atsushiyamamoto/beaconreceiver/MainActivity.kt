package com.atsushiyamamoto.beaconreceiver

import android.bluetooth.BluetoothManager
import android.bluetooth.le.BluetoothLeScanner
import android.bluetooth.le.ScanCallback
import android.bluetooth.le.ScanResult
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import android.Manifest.permission
import android.Manifest.permission.ACCESS_FINE_LOCATION
import android.Manifest.permission.ACCESS_COARSE_LOCATION
import android.support.v4.app.ActivityCompat
import android.content.pm.PackageManager
import android.support.v4.content.ContextCompat
import android.widget.TextView
import org.altbeacon.beacon.*
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(), BeaconConsumer {

    private lateinit var bluetoothLeScanner: BluetoothLeScanner
    private lateinit var scanCallback: IBeaconScanCallback
    private lateinit var beaconManager: BeaconManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val bluetoothManager = getSystemService(BLUETOOTH_SERVICE) as BluetoothManager
        val bluetoothAdapter = bluetoothManager.adapter

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            Toast.makeText(this, "BLE is not supported.", Toast.LENGTH_SHORT).show();
            finish();
        }

        if (!bluetoothAdapter.isEnabled) {
            // TODO: Show alert to have the user turn on Bluetooth.

            return;
        }

// TODO: requestPermissions()
//        https@ //stackoverflow.com/questions/32708374/bluetooth-le-scanfilters-dont-work-on-android-m

        scanCallback = IBeaconScanCallback()
        bluetoothLeScanner = bluetoothAdapter.bluetoothLeScanner
        bluetoothLeScanner.startScan(scanCallback)

        beaconManager = BeaconManager.getInstanceForApplication(this)
        beaconManager.beaconParsers.add(BeaconParser().setBeaconLayout("m:2-3=0215,i:4-19,i:20-21,i:22-23,p:24-24"))

        val permissionCheck = ContextCompat.checkSelfPermission(this, ACCESS_FINE_LOCATION)
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, ACCESS_FINE_LOCATION)) {
                Toast.makeText(this, "The permission to get BLE location data is required", Toast.LENGTH_SHORT).show()
            } else {
                ActivityCompat.requestPermissions(this, arrayOf(ACCESS_COARSE_LOCATION, ACCESS_FINE_LOCATION), 1)
            }
        } else {
            Toast.makeText(this, "Location permissions already granted", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroy() {
        bluetoothLeScanner.stopScan(scanCallback)

        super.onDestroy()
    }

    override fun onBeaconServiceConnect() {

        val monitorNotifier = object: MonitorNotifier {
            override fun didDetermineStateForRegion(p0: Int, p1: Region?) {
                println("didDetermineStateForRegion")

                // Should send state to server with this method.

                val UUID = "F202483B-D784-5B4E-BEF0-9AE63FD81F89"
                if (p1?.id1?.toUuid().let { it }.toString().toUpperCase() != UUID) {
                    return
                }

                mainTextViewState.text = when (p0) {
                    MonitorNotifier.INSIDE -> "Inside"
                    MonitorNotifier.OUTSIDE -> "Outside"
                    else -> ""
                }
                mainTextViewUuid.text = UUID
            }

            override fun didEnterRegion(p0: Region?) {
                println("didEnterRegion")

                // It's not fired when you wake your device up.
                mainTextViewLog.text = "${mainTextViewLog.text}\nEnter"
            }

            override fun didExitRegion(p0: Region?) {
                println("didExitRegion")

                // It's not fired when you wake your device up.
                mainTextViewLog.text = "${mainTextViewLog.text}\nExit"
            }
        }

        beaconManager.addMonitorNotifier(monitorNotifier)

        try {
            val region = Region("F202483B-D784-5B4E-BEF0-9AE63FD81F89", Identifier.parse("F202483B-D784-5B4E-BEF0-9AE63FD81F89"), null, null)
            beaconManager.startMonitoringBeaconsInRegion(region)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    override fun onPause() {
        super.onPause()
        beaconManager.unbind(this)
    }

    override fun onResume() {
        super.onResume()
        beaconManager.bind(this)
    }
}

final class IBeaconScanCallback: ScanCallback() {

    override fun onBatchScanResults(results: MutableList<ScanResult>?) {
        super.onBatchScanResults(results)
        println("onBatchScanResults")
        // If you set BLE delay(ScanSetttins$Builder#setReportDelay)
    }

    override fun onScanResult(callbackType: Int, result: ScanResult?) {
        super.onScanResult(callbackType, result)
        println("onScanResult")
        // If you set BLE delay(ScanSetttins$Builder#setReportDelay)
    }

    override fun onScanFailed(errorCode: Int) {
        super.onScanFailed(errorCode)
        // When error occurs
        val errorMessage = when (errorCode) {
            SCAN_FAILED_ALREADY_STARTED -> "既にBLEスキャンを実行中です"
            SCAN_FAILED_APPLICATION_REGISTRATION_FAILED -> "BLEスキャンを開始できませんでした"
            SCAN_FAILED_FEATURE_UNSUPPORTED -> "BLEの検索をサポートしていません"
            SCAN_FAILED_INTERNAL_ERROR -> "内部エラーが発生しました"
            else -> ""
        }

        println("onScanFailed")
        println(errorMessage)
    }
}
