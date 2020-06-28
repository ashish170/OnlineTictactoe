package com.example.tictactoe

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap


class MainActivity : AppCompatActivity() {
    var myEmail:String?=null

    private var database = FirebaseDatabase.getInstance()
    private var mAuth: FirebaseAuth? = null
    var myRef = database.reference
    private var mFirebaseAnalytics: FirebaseAnalytics? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        mFirebaseAnalytics = FirebaseAnalytics.getInstance(this);

        var b:Bundle=intent.extras
        myEmail=b.getString("email")
        income()
    }

    fun click(view:View)
    {
        val bu = view as Button
        var cellid=0
        when(bu.id)
        {
            R.id.button ->cellid = 1
            R.id.button2 ->cellid = 2
            R.id.button3 ->cellid = 3
            R.id.button4 ->cellid = 4
            R.id.button5 ->cellid = 5
            R.id.button6 ->cellid = 6
            R.id.button7 ->cellid = 7
            R.id.button8 ->cellid = 8
            R.id.button9 ->cellid = 9

        }
//        Log.d("cellid",cellid.toString())
//        Log.d("buClicked",bu.id.toString())
        myRef.child("playOnline").child(sessId!!).child(cellid.toString()).setValue(myEmail)

    }
    var activePlayer=1
    var player1= ArrayList<Int>()
    var player2=ArrayList<Int>()
    fun playgame(cellid:Int, bu:Button)
    {
        if(activePlayer==1)
        {
            bu.text="X"
            bu.setBackgroundResource(R.color.x)
            player1.add(cellid)
            activePlayer=2

        }
        else
        {
            bu.text="O"
            bu.setBackgroundResource(R.color.o)
            player2.add(cellid)
            activePlayer=1
        }
        bu.isEnabled=false
        checkwinner()
    }
    fun checkwinner()
    {

        var winner=-1
        if(player1.contains(1) && player1.contains(2) && player1.contains(3))
            winner=1
        if(player2.contains(1) && player2.contains(2) && player2.contains(3))
            winner=2
        if (player1.contains(4) && player1.contains(5) && player1.contains(6))
            winner=1
        if(player2.contains(4) && player2.contains(5) && player2.contains(6))
            winner=2
        if(player1.contains(7) && player1.contains(8) && player1.contains(9))
            winner=1
        if (player2.contains(7) && player2.contains(8) && player2.contains(9))
            winner=2
        if(player1.contains(1) && player1.contains(4) && player1.contains(7))
            winner=1
        if(player2.contains(1) && player2.contains(4) && player2.contains(7))
            winner=2
        if(player1.contains(2) && player1.contains(5) && player1.contains(8))
            winner=1
        if(player2.contains(2) && player2.contains(5) && player2.contains(8))
            winner=2
        if(player1.contains(3) && player1.contains(6) && player1.contains(9))
            winner=1
        if(player2.contains(3) && player2.contains(6) && player2.contains(9))
            winner=2
        if(player1.contains(1) && player1.contains(5) && player1.contains(9))
            winner=1
        if(player2.contains(1) && player2.contains(5) && player2.contains(9))
            winner=2
        if(player1.contains(3) && player1.contains(5) && player1.contains(7))
            winner=1
        if(player2.contains(3) && player2.contains(5) && player2.contains(7))
            winner=2
        if(winner==1) {
            Toast.makeText(this, "PLAYER 1 WINS", Toast.LENGTH_LONG).show()
            restart()
        }

        else if(winner==2) {
            Toast.makeText(this, "PLAYER 2 WINS", Toast.LENGTH_LONG).show()
            restart()
        }

    }
    fun autoplay(cellid:Int)
    {

        var bu:Button?
        bu=when(cellid)
        {
            1-> button
            2-> button2
            3-> button3
            4-> button4
            5-> button5
            6-> button6
            7-> button7
            8-> button8
            9-> button9
            else->button

        }
        playgame(cellid,bu)

    }
    fun restart()
    {
        activePlayer=1
        player1.clear()
        player2.clear()
        for (cellid in 1..9) {
            var bu:Button?
            bu=when(cellid)
            {
                1-> button
                2-> button2
                3-> button3
                4-> button4
                5-> button5
                6-> button6
                7-> button7
                8-> button8
                9-> button9
                else->button

        }
            bu.text=""
            bu.setBackgroundResource(R.color.buttoncolor)
            bu.isEnabled=true
        }
    }

    fun accept(view:View)
    {
        var email=name.text.toString()
        myRef.child("users").child(split(email)).child("request").push().setValue(myEmail)
        playOnline(split(email)+split(myEmail!!))
        playSym="O"

    }
    fun request(view:View)
    {
        var email=name.text.toString()
        myRef.child("users").child(split(email)).child("request").push().setValue(myEmail)
        playOnline(split(myEmail!!)+split(email))
        playSym="X"
    }
    var sessId:String?=null
    var playSym:String?=null
    fun playOnline(sessId:String)
    {
        this.sessId=sessId
        myRef.child("playOnline").removeValue()
        myRef.child("playOnline").child(sessId)
            .addValueEventListener(object:ValueEventListener
            {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    try {
                        player1.clear()
                        player2.clear()
                        val td=p0!!.value as HashMap<String,Any>
                        if(td!=null)
                        {
                            var value:String
                            for(key in td.keys)
                            {
                                value=td[key] as String
                                if(value!=myEmail)
                                {
                                    activePlayer=if(playSym=="X") 1 else 2
                                }
                                else
                                {
                                    activePlayer=if(playSym=="X") 2 else 1
                                }
                                autoplay(key.toInt())


                            }
                        }
                    }
                    catch (Ex:Exception)
                    {}
                }

            })
    }
    fun split(str:String):String
    {
        var sp=str.split("@")
        return sp[0]
    }
    var num=0
    fun income()
    {
        myRef.child("users").child(split(myEmail!!)).child(("request"))
            .addValueEventListener(object:ValueEventListener
            {
                override fun onCancelled(p0: DatabaseError) {
                }

                override fun onDataChange(p0: DataSnapshot) {
                    try {
                        val td=p0!!.value as HashMap<String,Any>
                        if(td!=null)
                        {
                            var value:String
                            for(key in td.keys)
                            {
                                value=td[key] as String
                                name.setText(value)
                                val notifyme=Notifications()
                                notifyme.Notify(applicationContext,value+"play game",num)
                                num++
                                myRef.child("users").child(split(myEmail.toString())).child("request").setValue(true)
                                break
                            }
                        }
                    }
                    catch (Ex:Exception)
                    {}
                }

            })
    }



}




