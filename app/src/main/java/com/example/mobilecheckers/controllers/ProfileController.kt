package com.example.mobilecheckers.controllers

import DatabaseHelper
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.example.mobilecheckers.MainActivity
import com.example.mobilecheckers.ProfileActivity
import com.example.mobilecheckers.R
import com.example.mobilecheckers.customComponents.StatTextField
import com.example.mobilecheckers.models.Player

class ProfileController(val profileActivity: ProfileActivity) {
    private var id:Long = -1
    init {
        val dataId = profileActivity.intent.getStringExtra("playerId")
        if(dataId!=null){
            this.id = dataId.toLong()
        }
        else{
            val sharedPref:SharedPreferences = this.profileActivity.getSharedPreferences("MySharedPreferences", Context.MODE_PRIVATE)
            val shId = sharedPref.getString("id","-1")!!.toLong()
            if(shId!=-1L){
                this.id = shId
            }
            else{
                val dbHelper = DatabaseHelper(profileActivity)
                this.id = dbHelper.addPlayer(Player("murlockiio",10,3,10.4))
                sharedPref.edit().putString("id",this.id.toString()).commit()
            }
        }
    }
    fun setupNavPanel(navLayout: LinearLayout){
        val backButton: Button = navLayout.findViewById(R.id.backButton)
        backButton.setOnClickListener({
            val intent: Intent = Intent(profileActivity, MainActivity::class.java)
            profileActivity.startActivity(intent)
        })
    }
    fun setupProfileLayout(view: View){
        val playerNameText = view.findViewById<TextView>(R.id.playerName)
        val playerWinsText = view.findViewById<StatTextField>(R.id.playerWinsText).findViewById<TextView>(R.id.numberField)
        val playerLosesText = view.findViewById<StatTextField>(R.id.playerLoseText).findViewById<TextView>(R.id.numberField)
        val playerMeanMovesText = view.findViewById<StatTextField>(R.id.playerMeanMovesText).findViewById<TextView>(R.id.numberField)

        val dbHelper = DatabaseHelper(profileActivity)
        val currentPlayer = dbHelper.getPlayerById(this.id)
        playerNameText.text = currentPlayer!!.getCorrectName()
        playerWinsText.text = currentPlayer.wins.toString()
        playerLosesText.text = currentPlayer.losses.toString()
        playerMeanMovesText.text = currentPlayer.averageMoves.toString()
    }
}