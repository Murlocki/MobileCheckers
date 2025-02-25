package com.example.mobilecheckers.models

import android.os.Parcel
import android.os.Parcelable

data class Player(
    val nickname: String,
    val wins: Int,
    val losses: Int,
    val averageMoves: Double,
    val id: Long = 0
) : Parcelable {

    // Рейтинг рассчитывается как сумма побед, поражений и среднего числа ходов
    val rating: Double
        get() = wins - losses + averageMoves

    // Конструктор для Parcel
    private constructor(parcel: Parcel) : this(
        nickname = parcel.readString() ?: "",
        wins = parcel.readInt(),
        losses = parcel.readInt(),
        averageMoves = parcel.readDouble(),
        id = parcel.readLong()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(nickname)
        parcel.writeInt(wins)
        parcel.writeInt(losses)
        parcel.writeDouble(averageMoves)
        parcel.writeLong(id)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Player> {
        override fun createFromParcel(parcel: Parcel): Player {
            return Player(parcel)
        }

        override fun newArray(size: Int): Array<Player?> {
            return arrayOfNulls(size)
        }
    }
    fun getCorrectName() = "${this.nickname}@${this.id}"
}



