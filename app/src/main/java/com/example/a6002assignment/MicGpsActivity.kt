package com.example.a6002assignment

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.speech.RecognizerIntent
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*


class MicGpsActivity : AppCompatActivity(), LocationListener {
    private val REQUEST_CODE_MIC = 1
    var storage = FirebaseStorage.getInstance()
    var storageRef = storage.getReferenceFromUrl("gs://assignment-d3570.appspot.com")


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mic_gps)
        val gpsButton = findViewById<Button>(R.id.btn_gps)
        val micButton = findViewById<Button>(R.id.btn_mic)
        val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)

        //gps part
        gpsButton.setOnClickListener {
            //request permission
            request.launch(Manifest.permission.ACCESS_FINE_LOCATION)
            //Check if the application has permissions when the gpsButton is clicked
            //If the permission already exists, use the requestLocationUpdates method to get the user's current location
            if ( permission == PackageManager.PERMISSION_GRANTED) {
                val locationManager = getSystemService(Context.LOCATION_SERVICE) as LocationManager
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 1000, 0f, this)
            }
        }


        //mic part
        micButton.setOnClickListener {
                //Call the speech recognizer when the micButton is clicked
                val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH)
                startActivityForResult(intent, REQUEST_CODE_MIC)
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

    //Display speech recognition results and upload the results to realtime database
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        val resultView = findViewById<TextView>(R.id.tv_result)
        //Get user ID and unique key from CameraActivity
        val getIntent = intent
        val uid = intent.getStringExtra("uid")
        val key = intent.getStringExtra("key")

        super.onActivityResult(requestCode, resultCode, data)
        //checking request code with result code
        if (requestCode == REQUEST_CODE_MIC) {
            //Check if data is null
            if (data != null) {
                //Extract data from array list
                val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<String>
                //Display the result in resultView
                resultView.text = result[0]

                val uploadButton2: Button = findViewById(R.id.btn_upload2)
                uploadButton2.setOnClickListener {
                    //Use the setValue method to write result to the specific location of the firebase realtime database,
                    //uid and key Get from CameraActivity
                    var database: DatabaseReference = Firebase.database.reference
                    database.child("user").child(uid.toString()).child(key.toString())
                        .child("comment").setValue(result[0])
                    Toast.makeText(applicationContext, "Upload Succeeded", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    //Upload the user's current location to the realtime database
    override fun onLocationChanged(p0: Location) {
        val textLatitude = findViewById<TextView>(R.id.tv_latitude)
        val textLongitude = findViewById<TextView>(R.id.tv_longitude)
        val textDetail = findViewById<TextView>(R.id.text_detail)
        val gpsButton = findViewById<Button>(R.id.btn_gps)
        val btnDetail = findViewById<Button>(R.id.detail)
        //Get user ID and unique key from CameraActivity
        val uid = intent.getStringExtra("uid")
        val key = intent.getStringExtra("key")

        //Text view displays latitude and longitude. When the user's location changes, latitude and longitude will also change
        textLatitude.text = p0.latitude.toString()
        textLongitude.text = p0.longitude.toString()

        //Use the Geocoder method to convert latitude and longitude into addresses
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses: List<Address> = geocoder.getFromLocation(p0.latitude, p0.longitude, 1)
        val address: String = addresses[0].getAddressLine(0)

        gpsButton.setOnClickListener {
            //Use the setValue method to write latitude, longitude and location to the specific location of the firebase realtime database,
            //uid and key Get from CameraActivity
            var database: DatabaseReference = Firebase.database.reference
            database.child("user").child(uid.toString()).child(key.toString()).child("latitude")
                .setValue(p0.latitude)
            database.child("user").child(uid.toString()).child(key.toString()).child("longitude")
                .setValue(p0.longitude)
            database.child("user").child(uid.toString()).child(key.toString()).child("location")
                .setValue(address)
            Toast.makeText(applicationContext,"Upload Succeeded",Toast.LENGTH_LONG).show()
        }
        btnDetail.setOnClickListener {
            textDetail!!.text = address
        }
    }
}

