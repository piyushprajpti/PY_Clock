package com.piyushprajpti.pyclock.service.alarm

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp

class AlarmActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val time = intent.getStringExtra("time")
        val message = intent.getStringExtra("message")

        setContent {
            Column(modifier = Modifier.fillMaxSize()) {
                Text(text = time.toString(), fontSize = 50.sp)
                Text(text = message.toString(), fontSize = 50.sp)
            }
        }
    }
}