package com.lf.esharing

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.lf.esharing.network.SocketIOClient

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

    }

    override fun onDestroy() {
        super.onDestroy()
        SocketIOClient.disconnect()
    }
}