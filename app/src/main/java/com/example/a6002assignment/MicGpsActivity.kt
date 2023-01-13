package com.example.a6002assignment

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.location.*
import android.os.Bundle
import android.speech.RecognizerIntent
import android.view.View
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.FirebaseStorage
import java.util.*


class MicGpsActivity : AppCompatActivity(), LocationListener {
    private val REQUEST_CODE_MIC = 1
    var storage = FirebaseStorage.getInstance()
    var storageRef = storage.getReferenceFromUrl("gs://assignment-d3570.appspot.com")
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_mic_gps)
        val gpsButton = findViewById<Button>(R.id.btn_gps)
        val micButton = findViewById<Button>(R.id.btn_mic)
        val permission = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
        // Initialize Firebase Auth
        auth = Firebase.auth
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

        title = "Upload comment and filming location"
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
        //Get unique key from CameraActivity
        val key = intent.getStringExtra("key")
        //Get user ID from Firebase Authentication
        val uid = FirebaseAuth.getInstance().currentUser!!.uid

        super.onActivityResult(requestCode, resultCode, data)
        //checking request code with result code
        if (requestCode == REQUEST_CODE_MIC) {
            //Check if data is null
            if (data != null) {
                //Extract data from array list
                val result = data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS) as ArrayList<String>
                //Display the result in resultView
                resultView.text = "Your Comment: "+result[0]

                val uploadMicButton: Button = findViewById(R.id.btn_uploadMic)
                uploadMicButton.setOnClickListener {
                    //Use the setValue method to write result to the specific location of the firebase realtime database,
                    //uid get from Firebase Authentication and key get from CameraActivity
                    var database: DatabaseReference = Firebase.database.reference
                    database.child("user").child(uid.toString()).child(key.toString())
                        .child("comment").setValue(result[0])
                    Toast.makeText(applicationContext, "Upload Comment Succeeded", Toast.LENGTH_LONG).show()
                }
            }
        }
    }

    //Upload the user's current location to the realtime database
    override fun onLocationChanged(p0: Location) {
        val textAddress = findViewById<TextView>(R.id.tv_address)
        val uploadGpsButton = findViewById<Button>(R.id.btn_uploadGps)
        //Get unique key from CameraActivity
        val key = intent.getStringExtra("key")
        //Get user ID from Firebase Authentication
        val uid = FirebaseAuth.getInstance().currentUser!!.uid

        //Use the Geocoder method to convert latitude and longitude into addresses
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses: List<Address> = geocoder.getFromLocation(p0.latitude, p0.longitude, 1)
        val address: String = addresses[0].getAddressLine(0)

        //Text view displays address. When the user's location changes, address will also change
        textAddress!!.text = "Your Address: "+address

        uploadGpsButton.setOnClickListener {
            //Use the setValue method to write latitude, longitude and address to the specific location of the firebase realtime database,
            //uid get from Firebase Authentication and key get from CameraActivity
            var database: DatabaseReference = Firebase.database.reference
            database.child("user").child(uid).child(key.toString()).child("latitude")
                .setValue(p0.latitude)
            database.child("user").child(uid).child(key.toString()).child("longitude")
                .setValue(p0.longitude)
            database.child("user").child(uid).child(key.toString()).child("address")
                .setValue(address)
            Toast.makeText(applicationContext,"Upload Address Succeeded",Toast.LENGTH_LONG).show()
        }
    }

    fun gotoMain(view: View) {
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
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

