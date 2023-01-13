package com.example.a6002assignment

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class PhotoDetailActivity : AppCompatActivity(), LocationListener {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_photo_detail)
        // Initialize Firebase Auth
        auth = Firebase.auth
        val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        //request permission
        request.launch(Manifest.permission.ACCESS_FINE_LOCATION)
        //Check if the application has permissions when the gpsButton is clicked
        //If the permission already exists, use the requestLocationUpdates method to get the user's current location
        if ( permission == PackageManager.PERMISSION_GRANTED) {
            val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
            locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0f, this)
        }
        title = "View photo details"
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

        val viewDistance = findViewById<TextView>(R.id.tv_distance)
        val viewPicUrl = findViewById<TextView>(R.id.tv_url)
        val viewLatitude = findViewById<TextView>(R.id.tv_latitude)
        val viewLongitude = findViewById<TextView>(R.id.tv_longitude)
        val viewComment = findViewById<TextView>(R.id.tv_detailComment)
        val viewAddress = findViewById<TextView>(R.id.tv_detailAddress)

        //Get details from PhotoAdapter
        val picUrl = intent.getStringExtra("pic")
        val comment = intent.getStringExtra("comment")
        val address = intent.getStringExtra("address")
        var photoLatitude = intent.getDoubleExtra("latitude", 0.0)
        var photoLongitude = intent.getDoubleExtra("longitude",0.0)

        //Use the distanceBetween() method to calculate the approximate distance
        //between the location of the photo taken and the user's current location
        val float = FloatArray(1)
        Location.distanceBetween(photoLatitude, photoLongitude, p0.latitude, p0.longitude,float)

        viewDistance!!.text = float[0].toString()+"(m)"
        viewPicUrl!!.text = picUrl
        viewLatitude!!.text = photoLatitude.toString()
        viewLongitude!!.text = photoLongitude.toString()
        viewComment!!.text = comment
        viewAddress!!.text = address

    }


    public override fun onStart() {
        super.onStart()
        //Use the getCurrentUser() method of Firebase Authentication
        // to check whether the user is logged in.
        // If the user is not logged in, force the user to return to MainActivity2
        val currentUser = auth.currentUser
        if(currentUser == null){
            val intent = Intent(this, MainActivity2::class.java)
            startActivity(intent)
        }
    }
}