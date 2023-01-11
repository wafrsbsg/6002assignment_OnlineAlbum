package com.example.a6002assignment

import Users
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class PhotoActivity : AppCompatActivity() {

        override fun onCreate(savedInstanceState: Bundle?) {
            super.onCreate(savedInstanceState)
            setContentView(R.layout.activity_photo)
            var recyclerPhoto: RecyclerView =findViewById(R.id.ir_photo)
            var usersArrayList : ArrayList<Users> = arrayListOf<Users>()
            //Get user ID from MianActivity
            val uid = intent.getStringExtra("uid")
            //Set photos to linear layout
            recyclerPhoto.layoutManager = LinearLayoutManager(this)

            //uid Get from MainActivity
            var database: DatabaseReference = FirebaseDatabase.getInstance().reference
            var databaseReference = database.child("user").child(uid.toString())
            //use the addValueEventListener() method to add a ValueEventListener to databaseReference
            var photoListener = object : ValueEventListener{
                //Use the onDataChange() method to read a static snapshot of the contents at a given path
                override fun onDataChange(dataSnapshot: DataSnapshot) {
                    for (usersSnapshot in dataSnapshot.children){
                        //Get object and use the values to update
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
        }
}