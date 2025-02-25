package com.example.mobilecheckers.models

import android.os.Parcel
import android.os.Parcelable

data class Checker(var row: Int, var col: Int, val isWhite: Boolean,var isQueen:Boolean=false) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readByte() != 0.toByte()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(row)
        parcel.writeInt(col)
        parcel.writeByte(if (isWhite) 1 else 0)
    }

    override fun describeContents(): Int = 0

    companion object CREATOR : Parcelable.Creator<Checker> {
        override fun createFromParcel(parcel: Parcel): Checker {
            return Checker(parcel)
        }

        override fun newArray(size: Int): Array<Checker?> {
            return arrayOfNulls(size)
        }
    }

    override fun equals(other: Any?): Boolean {
        if(other == null) return false

        val second = other as Checker
        return this.row == second.row && this.col == second.col

    }

    fun moveChecker(row:Int,col:Int){
        this.row = row;
        this.col = col;
        if(row==0){
            this.upgradeToQueen()
        }
    }
    fun upgradeToQueen(){
        this.isQueen = true
    }
    fun getPossibleMoves():List<Pair<Int,Int>>{
        if(!isQueen){
            return listOf(Pair(-1, -1), Pair(-1, 1))
        }
        else{
            val res = mutableListOf<Pair<Int,Int>>()
            for(i in 1..8){
                res.add(Pair(i,i))
                res.add(Pair(-i,i))
                res.add(Pair(i,-i))
                res.add(Pair(-i,-i))
            }
            return res
        }
    }
    fun getPossibleAttackMoves():List<Pair<Int,Int>>{
        if(!isQueen){
            return listOf(Pair(-1, -1), Pair(-1, 1), Pair(1, -1), Pair(1, 1))
        }
        else{
            val res = mutableListOf<Pair<Int,Int>>()
            for(i in 1..8){
                res.add(Pair(i,i))
                res.add(Pair(-i,i))
                res.add(Pair(i,-i))
                res.add(Pair(-i,-i))
            }
            return res
        }
    }
}
