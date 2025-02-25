package com.example.mobilecheckers
import DatabaseHelper
import com.example.mobilecheckers.adapters.PlayerAdapter
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ListView
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
import com.example.mobilecheckers.controllers.RatingViewController
import com.example.mobilecheckers.models.Player
import com.example.mobilecheckers.ui.theme.MobileCheckersTheme


class RatingActivity : ComponentActivity() {
    private lateinit var ratingViewController:RatingViewController

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MobileCheckersTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    RatingScreen(Modifier.padding(innerPadding))
                }
            }
        }
        ratingViewController = RatingViewController(this)
        ratingViewController.resetPlayersState(savedInstanceState)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun RatingScreen(modifier: Modifier = Modifier) {
        AndroidView(
            factory = { context ->
                val inflater = LayoutInflater.from(context)
                val view = inflater.inflate(R.layout.rating_layout,null)

                ratingViewController.settingPlayersFromDB(view)

                val navLayout: LinearLayout = view.findViewById(R.id.navPanel)
                ratingViewController.setupNavPanel(navLayout)

                view
            },
            modifier = modifier.fillMaxSize()
        )
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        this.ratingViewController.savePlayersState(outState)
    }
}