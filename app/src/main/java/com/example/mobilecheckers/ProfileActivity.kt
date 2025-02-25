package com.example.mobilecheckers
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.LinearLayout
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
import com.example.mobilecheckers.controllers.ProfileController
import com.example.mobilecheckers.ui.theme.MobileCheckersTheme


class ProfileActivity : ComponentActivity() {
    private var profileController: ProfileController? = null;
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MobileCheckersTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    ProfileScreen(Modifier.padding(innerPadding))
                }
            }
        }
        this.profileController = ProfileController(this)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun ProfileScreen(modifier: Modifier = Modifier) {
        AndroidView(
            factory = { context ->
                val inflater = LayoutInflater.from(context)
                val view = inflater.inflate(R.layout.profile_layout,null)
                this.profileController!!.setupProfileLayout(view)

                val navLayout: LinearLayout = view.findViewById(R.id.navPanel)
                this.profileController!!.setupNavPanel(navLayout)

                println(intent.getStringExtra("playerId"))

                view
            },
            modifier = modifier.fillMaxSize()
        )
    }
}