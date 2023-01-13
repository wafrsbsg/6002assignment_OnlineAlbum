package com.example.a6002assignment

import Users
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase


class RegisterActivity : AppCompatActivity() {

    private lateinit var registerAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)
        //Initialize Firebase Auth
        registerAuth = Firebase.auth
        title = "Register"
    }

    fun register(view: View){
        val textRegisterEmail = findViewById<TextView>(R.id.et_registerEmail)
        val textRegisterPassword = findViewById<TextView>(R.id.et_registerPassword)
        val registerEmail = textRegisterEmail.text.toString()
        val registerPassword = textRegisterPassword.text.toString()

        //Use the createUserWithEmailAndPassword() method from Firebase Authentication
        // to let new users register with email address and password
        registerAuth.createUserWithEmailAndPassword(registerEmail,registerPassword).addOnCompleteListener(this) { task ->
            if(task.isSuccessful){
                val user = Firebase.auth.currentUser
                //Go to MainActivity after successful registration
                user?.let {
                    val intent= Intent(this,MainActivity::class.java)
                    startActivity(intent)
                    finish()
                }
            }
        }.addOnFailureListener { exception ->
            // If sign in fails, display a message to the user.
            Toast.makeText(applicationContext,exception.localizedMessage,Toast.LENGTH_LONG).show()
        }
    }

    fun goToLogin(view: View){
        val intent= Intent(this,LoginActivity::class.java)
        startActivity(intent)
    }
}

