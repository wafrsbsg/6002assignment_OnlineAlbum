package com.example.a6002assignment

import Users
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase


class PhotoActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_photo)
            var recyclerPhoto: RecyclerView =findViewById(R.id.ir_photo)
            var usersArrayList : ArrayList<Users> = arrayListOf<Users>()
            // Initialize Firebase Auth
            auth = Firebase.auth
            //Get user ID from Firebase Authentication
            val uid = FirebaseAuth.getInstance().currentUser!!.uid
            //Set photos to linear layout
            recyclerPhoto.layoutManager = LinearLayoutManager(this)

            //uid Get from Firebase Authentication
            var database: DatabaseReference = FirebaseDatabase.getInstance().reference
            var databaseReference = database.child("user").child(uid)
            //use the addValueEventListener() method to add a ValueEventListener to databaseReference
            var photoListener = object : ValueEventListener{
                //Use the onDataChange() method to read a static snapshot of the contents at a given path
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (usersSnapshot in dataSnapshot.children){
                        //Get object and use the values to update UI
                        val users = usersSnapshot.getValue(Users::class.java)
                        if (users != null) {
                            usersArrayList.add(users)
                        }
                    }
                    //Set the data behind RecyclerView recyclerPhoto
                    recyclerPhoto.adapter = PhotoAdapter(usersArrayList, this@PhotoActivity)
                }

                override fun onCancelled(error: DatabaseError) {
                    //Failed
                    Toast.makeText(applicationContext, "Failed", Toast.LENGTH_LONG).show()
                }
            }
            databaseReference.addValueEventListener(photoListener)
            title = "View photo(with comment and location)"
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