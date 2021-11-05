package com.example.qrcodegenerator

import android.Manifest
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Color
import android.net.wifi.WifiManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import kotlinx.android.synthetic.main.activity_main.*
import net.glxn.qrgen.android.QRCode.from
import androidx.core.content.ContextCompat.checkSelfPermission as checkSelfPermission1


class MainActivity : AppCompatActivity(), View.OnClickListener {

    private var qrImage : Bitmap? = null
    val EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE = 1

    @SuppressLint("ResourceType")
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        btn_text.setOnClickListener(this)
        btn_vCard.setOnClickListener(this)
        btn_generateQR.setOnClickListener(this)

        //Check for  permission
        if (!checkPermissionForExternalStorage()) {
            requestPermissionForExternalStorage()
        }


    }

    override fun onClick(v: View?) {
        when(v?.id)
        {
            R.id.btn_text->
            {
//                input_text.visibility = View.VISIBLE
                view_rssi.visibility = View.VISIBLE
                layout_textcode.visibility = View.GONE
                btn_generateQR.visibility = View.VISIBLE
            }
            R.id.btn_vCard->
            {
//                input_text.visibility = View.GONE
                view_rssi.visibility = View.GONE
                layout_textcode.visibility = View.VISIBLE
                btn_generateQR.visibility = View.VISIBLE

            }
            R.id.btn_generateQR->
            {
                if(layout_textcode.visibility == View.VISIBLE) {
                    if(!input_textcode.text.toString().isEmpty())
                    {
                        generateQRCode()
                    }
                    else
                    {
                        input_textcode.setError("This field is required")
                    }

                }
                else
                {
                    generateQRCode()
                }
            }

        }
    }

    //Function for Generating QR code
    @SuppressLint("SetTextI18n")
    fun generateQRCode()
    {
        if(layout_textcode.visibility == View.VISIBLE)
        {
            val text2Qr = input_textcode.text.toString()
            qrImage =
                from(text2Qr).bitmap()
            if(qrImage != null)
            {
                imageView_qrCode.setImageBitmap(qrImage)
            }

            input_textcode.text = null
        }
        else //  if(input_text.visibility == View.GONE)
        {
            val wifiManager = applicationContext.getSystemService(WIFI_SERVICE) as WifiManager
            val rssi = wifiManager.connectionInfo.rssi.toString()
            qrImage =
                from(rssi).bitmap()
            if(qrImage != null)
            {
                imageView_qrCode.setImageBitmap(qrImage)
            }
            view_rssi.text = "Wifi Strength is $rssi dbM"
            if(rssi.toInt() < -70) {
                view_rssi.setTextColor(Color.RED)
            }
            else{
                view_rssi.setTextColor(Color.GREEN)
            }
        }
    }

    //function for requesting storage access
    fun requestPermissionForExternalStorage() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_WIFI_STATE) || ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_NETWORK_STATE)) {
            Toast.makeText(this, "External Storage permission needed. Please allow in App Settings for additional functionality.", Toast.LENGTH_LONG).show()
        } else {
            ActivityCompat.requestPermissions(this, arrayOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE), EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE)
            ActivityCompat.requestPermissions(this, arrayOf<String>(Manifest.permission.ACCESS_WIFI_STATE),EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE)
            ActivityCompat.requestPermissions(this, arrayOf<String>(Manifest.permission.ACCESS_NETWORK_STATE),EXTERNAL_STORAGE_PERMISSION_REQUEST_CODE)


        }
    }
    //fuunction for checking storage permission
    fun checkPermissionForExternalStorage(): Boolean {

        val result = checkSelfPermission1(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)
        return result == PackageManager.PERMISSION_GRANTED
    }


}
