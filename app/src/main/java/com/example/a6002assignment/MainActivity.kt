package com.example.a6002assignment

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        // Initialize Firebase Auth
        auth = Firebase.auth
    }
        fun goToCamera(view: View) {
            val intent = Intent(this, CameraActivity::class.java)
            startActivity(intent)
        }

        fun goToPicture(view: View) {
            val intent = Intent(this, PhotoActivity::class.java)
            startActivity(intent)
        }

        fun logout(view: View) {
            Firebase.auth.signOut()
            finishAffinity()
            val intent = Intent(this, MainActivity2::class.java)
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