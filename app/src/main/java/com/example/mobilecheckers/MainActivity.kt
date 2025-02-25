package com.example.mobilecheckers

import DatabaseHelper
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.example.mobilecheckers.models.Player
import com.example.mobilecheckers.ui.theme.MobileCheckersTheme

class MainActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MobileCheckersTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MainScreen(Modifier.padding(innerPadding))
                }
            }
        }

    }
    @Composable
    fun MainScreen(modifier: Modifier = Modifier){
        AndroidView(
            factory = { context ->
                val inflater = LayoutInflater.from(context)
                val view = inflater.inflate(R.layout.main_screen_layout,null)
                val startGameButton = view.findViewById<Button>(R.id.startButton)
                startGameButton.setOnClickListener({
                    val intent: Intent = Intent(this, GameActivity::class.java)
                    startActivity(intent)
                })
                val profileButton = view.findViewById<Button>(R.id.profileButton)
                profileButton.setOnClickListener({
                    val intent: Intent = Intent(this, ProfileActivity::class.java)
                    startActivity(intent)
                })

                val ratingButton = view.findViewById<Button>(R.id.ratingButton)
                ratingButton.setOnClickListener({
                    val intent: Intent = Intent(this, RatingActivity::class.java)
                    startActivity(intent)
                })

                view
            },
            modifier = modifier.fillMaxSize()
        )
    }
}