package com.example.mobilecheckers.controllers

import DatabaseHelper
import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.ListView
import androidx.lifecycle.ViewModelProvider
import com.example.mobilecheckers.MainActivity
import com.example.mobilecheckers.R
import com.example.mobilecheckers.RatingActivity
import com.example.mobilecheckers.adapters.PlayerAdapter
import com.example.mobilecheckers.models.Checker
import com.example.mobilecheckers.models.Player
import com.example.mobilecheckers.models.PlayersListModel

class RatingViewController(private val ratingActivity: RatingActivity) {
    private var playerListModel: PlayersListModel = ViewModelProvider(ratingActivity).get(PlayersListModel::class.java)
    private var adapter: PlayerAdapter? = null
    fun settingPlayersFromDB(view: View){
        val players = playerListModel.players.value
        val listView: ListView = view.findViewById<ListView>(R.id.lvPlayers)
        adapter = players?.let { java.util.ArrayList(it) }
            ?.let { PlayerAdapter(ratingActivity, it) }
        if(playerListModel.currentExpanded.value != -1){
            this.adapter!!.setExpandedPost(playerListModel.currentExpanded.value)
        }
        listView.adapter = adapter
    }
    fun resetPlayersState(bundle: Bundle?){
        if (bundle != null) {
            val savedPlayers = bundle.getParcelableArrayList<Player>("players_state")
            val expanded = bundle.getInt("expanded_state")
            savedPlayers!!.let {
                playerListModel.restorePlayersState(it,expanded)
            }
        }
    }
    fun savePlayersState(outState: Bundle){
        playerListModel.setCurrentExpanded(adapter!!.getExpandedPost())
        val playersState = playerListModel.savePlayersState()
        outState.putParcelableArrayList("players_state", ArrayList(playersState.first))
        outState.putInt("expanded_state", playersState.second)
    }
    fun setupNavPanel(navLayout: LinearLayout){
        val backButton: Button = navLayout.findViewById(R.id.backButton)
        backButton.setOnClickListener({
            val intent: Intent = Intent(ratingActivity, MainActivity::class.java)
            playerListModel.resetPlayers()
            ratingActivity.startActivity(intent)
        })
    }
}