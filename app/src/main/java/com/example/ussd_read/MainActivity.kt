package com.example.ussd_read

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.*
import androidx.core.app.ActivityCompat
import com.hover.sdk.api.Hover
import com.hover.sdk.api.HoverParameters

class MainActivity : AppCompatActivity() {
     lateinit var session: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        setContentView(R.layout.activity_main)
        super.onCreate(savedInstanceState)


        val submit= findViewById<Button>(R.id.btnV)
        session=findViewById(R.id.session)

        submit.setOnClickListener(){
            try {
                checkMoney()
            }catch (e:Exception){
                Toast.makeText(this,"Error ${e.message}",Toast.LENGTH_LONG).show()
            }
        }
    }

    private fun checkMoney(){
        Hover.initialize(this)
        try {
            Log.d("MainActivity", "Sims are= " + Hover.getPresentSims(this))
            Log.d("MainActivity", "Hover actions are=  " + Hover.getAllValidActions(this))
        }catch (e: Exception){
            Log.d("MainActivity", "hover exception", e)

        }
        val i:Intent =HoverParameters.Builder(this)
                .request("288a7dbd")
                .setHeader("En cours")
                .initialProcessingMessage("Patientez svp...")
                .buildIntent()
        startActivityForResult(i, 0)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        try {
            val sessionTextArr = data?.getStringArrayExtra("session_messages")
            val uuid = data?.getStringExtra("uuid")
            var message:String=" "
            if (sessionTextArr != null) {
                for (i in sessionTextArr.indices){
                    message+=sessionTextArr[i]
                }
                session.text=message
            }
            Log.d("MainActivity", "Session message= $sessionTextArr")

            Log.d("MainActivity", "UUID= $uuid " )
        }catch (e: Exception){
            Log.d("Bug code","Message: ${e.message}")
        }
    }
}


