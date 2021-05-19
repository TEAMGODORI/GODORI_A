package com.example.godori.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import java.lang.Exception

class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        try {
            Thread.sleep(2000)
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
        catch (e : Exception){
            return;
        }
    }
}