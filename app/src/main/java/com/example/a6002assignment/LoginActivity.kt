package com.example.a6002assignment

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


class LoginActivity : AppCompatActivity() {

    private lateinit var loginAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        //Initialize Firebase Auth
        loginAuth = Firebase.auth
        title = "Login"
    }

    @SuppressLint("SuspiciousIndentation")
    fun login(view: View) {
        val textLoginEmail = findViewById<TextView>(R.id.et_loginEmail)
        val textLoginPassword = findViewById<TextView>(R.id.et_loginPassword)
        val loginEmail = textLoginEmail.text.toString()
        val loginPassword = textLoginPassword.text.toString()

            //Use the signInWithEmailAndPassword() method from Firebase Authentication
        // to let users login with email address and password
            loginAuth.signInWithEmailAndPassword(loginEmail, loginPassword)
                .addOnCompleteListener(this) { task ->
                    if (task.isSuccessful) {
                        //Get user ID from Firebase Authentication
                        val intent = Intent(this, MainActivity::class.java)
                        startActivity(intent)
                        finish()
                    }
                }.addOnFailureListener { exception ->
                    // If sign in fails, display a message to the user.
                    Toast.makeText(applicationContext,
                        exception.localizedMessage,
                        Toast.LENGTH_LONG).show()
                }
        }



    fun goToRegister(view: View) {
        val intent = Intent(this, RegisterActivity::class.java)
        startActivity(intent)
    }
}

