package com.example.mobilecheckers.models

import DatabaseHelper
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mobilecheckers.RatingActivity

class PlayersListModel(application: Application): AndroidViewModel(application) {
    val players = MutableLiveData<MutableList<Player>>()
    val currentExpanded = MutableLiveData<Int>()
    init {
        if(players.value.isNullOrEmpty()){
            loadPlayers()
        }
    }
    private fun loadPlayers() {
        val loadedPlayers = mutableListOf<Player>()
        // Добавьте данные игроков в список
        val dbHelper = DatabaseHelper(getApplication())



        // Получаем всех игроков из базы данных
        val playersFromDb = dbHelper.getAllPlayers()
        for (player in playersFromDb) {
            loadedPlayers.add(player)
        }
        players.value = loadedPlayers
        currentExpanded.value = -1
    }

    fun setCurrentExpanded(position:Int?){
        this.currentExpanded.value = position ?: -1
    }

    fun savePlayersState(): Pair<List<Player>,Int> {
        return Pair(players.value ?: emptyList(),currentExpanded.value ?: -1)
    }

    fun restorePlayersState(savedPlayers: List<Player>, saveCurrentExpanded:Int) {
        players.value = savedPlayers.toMutableList()
        currentExpanded.value = saveCurrentExpanded
    }
    fun resetPlayers(){
        players.value?.clear()
        currentExpanded.value = -1
    }
}