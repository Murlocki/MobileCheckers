package com.example.mobilecheckers
import CheckerView
import GameViewModel
import android.app.FragmentContainer
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.ShapeDrawable
import android.graphics.drawable.shapes.OvalShape
import android.os.Build
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.FrameLayout
import android.widget.GridLayout
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
import androidx.lifecycle.ViewModelProvider
import com.example.mobilecheckers.controllers.GameController
import com.example.mobilecheckers.fragments.CheckerBoard
import com.example.mobilecheckers.models.Checker
import com.example.mobilecheckers.ui.theme.MobileCheckersTheme
import kotlin.math.min

class GameActivity : ComponentActivity() {
    private lateinit var controller:GameController;
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MobileCheckersTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    GameScreen(Modifier.padding(innerPadding))
                }
            }
        }
        controller = GameController(this)
        controller.resetCheckerState(bundle = savedInstanceState)
    }
    @RequiresApi(Build.VERSION_CODES.O)
    @Composable
    fun GameScreen(modifier: Modifier = Modifier) {
        AndroidView(
            factory = { context ->
                val inflater = LayoutInflater.from(context)
                val view = inflater.inflate(R.layout.game_layout,null)

                val gridLayout: GridLayout = view.findViewById(R.id.checkersBoard);
                this.controller.setupCheckersBoard(gridLayout)

                val navLayout: LinearLayout = view.findViewById(R.id.navPanel)
                this.controller.setupNavPanel(navLayout)

                view
            },
            modifier = modifier.fillMaxSize()
        )
    }
    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        this.controller.saveCheckersState(outState)
    }

}