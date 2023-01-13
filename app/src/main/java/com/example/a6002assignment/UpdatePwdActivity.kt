package com.example.a6002assignment

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class UpdatePwdActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_update_pwd)
        // Initialize Firebase Auth
        auth = Firebase.auth
        title = "Update Password"
    }

    fun update(view: View){
        val user = Firebase.auth.currentUser
        val textNewPassword = findViewById<TextView>(R.id.et_newPassword)
        val newPassword = textNewPassword.text.toString()

        //Use the updatePassword() method from Firebase Authentication
        // to let users update password
        user!!.updatePassword(newPassword)
            .addOnCompleteListener { task ->
                if (task.isSuccessful) {
                    Toast.makeText(applicationContext, "Update Succeeded", Toast.LENGTH_LONG).show()
                }
            }.addOnFailureListener { exception ->
                // If sign in fails, display a message to the user.
                Toast.makeText(applicationContext,exception.localizedMessage,Toast.LENGTH_LONG).show()
            }
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
