package com.example.a6002assignment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat

class PhotoDetailActivity : AppCompatActivity(), LocationListener {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_detail)

        val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        //request permission
        request.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        //Check if the application has permissions when the gpsButton is clicked
        //If the permission already exists, use the requestLocationUpdates method to get the user's current location
        if ( permission == PackageManager.PERMISSION_GRANTED) {
            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0f, this)
        }
    }

    //request permission
    private val request = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { granted ->
        if (granted){
            //If the user grant, the application will be able to access the precise location
            Manifest.permission.ACCESS_FINE_LOCATION
        }else{
            //If the user does not grant, show toast
            Toast.makeText(applicationContext, "Please give permission", Toast.LENGTH_LONG).show()
        }
    }

    override fun onLocationChanged(p0: Location) {

        val textDistance = findViewById<TextView>(R.id.tv_distance)
        //Get latitude and longitude from PhotoAdapter
        var photoLatitude = intent.getDoubleExtra("latitude", 0.0)
        var photoLongitude = intent.getDoubleExtra("longitude",0.0)

        //Use the distanceBetween() method to calculate the approximate distance
        //between the location of the photo taken and the user's current location
        val float = FloatArray(1)
        Location.distanceBetween(photoLatitude, photoLongitude, p0.latitude, p0.longitude,float)

        textDistance!!.text = float[0].toString()

       // Toast.makeText(applicationContext,float[0].toString(), Toast.LENGTH_LONG).show()

    }
}