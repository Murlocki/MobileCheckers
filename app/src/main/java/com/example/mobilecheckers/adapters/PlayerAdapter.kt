package com.example.mobilecheckers.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import com.example.mobilecheckers.R
import com.example.mobilecheckers.customComponents.StatTextField
import com.example.mobilecheckers.models.Player

class PlayerAdapter(private val context: Context, private val players: List<Player>) : BaseAdapter() {

    private var expandedPosition: Int? = null

    override fun getCount(): Int = players.size
    override fun getItem(position: Int): Any = players[position]
    override fun getItemId(position: Int): Long = position.toLong()
    fun setExpandedPost(position: Int?){
        this.expandedPosition = if(position == -1) null else position
    }
    fun getExpandedPost():Int{
        return this.expandedPosition ?: -1
    }
    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        val view = convertView ?: LayoutInflater.from(context).inflate(R.layout.item_player, parent, false)

        val nicknameTextView = view.findViewById<TextView>(R.id.nicknameTextView)
        val ratingTextView = view.findViewById<TextView>(R.id.ratingTextView)
        val detailsLayout = view.findViewById<LinearLayout>(R.id.detailsLayout)
        val winsTextView= view.findViewById<StatTextField>(R.id.winsTextView)
        val lossesTextView = view.findViewById<StatTextField>(R.id.lossesTextView)
        val avgMovesTextView = view.findViewById<StatTextField>(R.id.avgMovesTextView)


        val player = players[position]

        val toProfileButton = view.findViewById<Button>(R.id.toProfileButton)
        toProfileButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_RUN)
            intent.putExtra("playerId",player.id.toString())
            context.startActivity(intent)
        }

        nicknameTextView.text = player.getCorrectName()
        ratingTextView.text = "Рейтинг: ${player.rating}"
        winsTextView.setCounter("${player.wins}")
        lossesTextView.setCounter("${player.losses}")
        avgMovesTextView.setCounter("${player.averageMoves}")

        // Устанавливаем видимость деталей в зависимости от того, открыт ли элемент
        detailsLayout.visibility = if (position == expandedPosition) View.VISIBLE else View.GONE

        // Обработчик нажатия
        view.setOnClickListener {
            expandedPosition = if (expandedPosition == position) null else position
            notifyDataSetChanged() // Перерисовка списка
        }

        return view
    }

}
