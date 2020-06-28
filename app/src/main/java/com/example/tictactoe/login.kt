package com.example.tictactoe

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.android.synthetic.main.activity_login.*


class login : AppCompatActivity() {
    private var database = FirebaseDatabase.getInstance()
    private var mAuth: FirebaseAuth? = null
    var myRef = database.reference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        mAuth = FirebaseAuth.getInstance();
    }
    fun bu(view:View)
    {
        val email= name.text.toString()
        val pass=password.text.toString()
        loginfire(email,pass)

    }
     fun loginfire(email:String,pass:String)

    {
        mAuth!!.createUserWithEmailAndPassword(email,pass)
            .addOnCompleteListener(this)
            {
                task ->
                if(task.isSuccessful) {
                    val cur=mAuth!!.currentUser
                    if (cur!=null)
                        myRef.child("users").child(split(cur.email.toString())).child("request").setValue(cur.uid)

                    Toast.makeText(this, "Login Accepted", Toast.LENGTH_SHORT).show()
                     load()

                }
                else
                    Toast.makeText(this,"Login Failed",Toast.LENGTH_SHORT).show()

            }

    }

    override fun onStart() {
        super.onStart()
        load()


        }
        fun load()
        {

            val cur=mAuth!!.currentUser
            if (cur!=null) {
                myRef.child("users").child(split(cur.uid)).setValue(cur.email)
                var intent = Intent(this, MainActivity::class.java)
                intent.putExtra("email",cur.email)
                intent.putExtra("id",cur.uid)
                startActivity(intent)
        }

    }
    fun split(str:String):String
    {
        var sp=str.split("@")
        return sp[0]
    }
}
