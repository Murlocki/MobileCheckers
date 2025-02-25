package com.example.mobilecheckers.controllers

import CheckerView
import android.view.View

class CheckerViewController(val checkerView: CheckerView) {
    fun checkerViewMoveAnimation(endX: Float, endY: Float, endCallBack: ()->Unit){
        // Устанавливаем начальные координаты для анимации
        checkerView.translationX = 0F
        checkerView.translationY = 0F
        checkerView.animate()
            .translationX(endX) // Абсолютная конечная позиция X
            .translationY(endY) // Абсолютная конечная позиция Y
            .setDuration(400)  // Длительность анимации
            .withEndAction {
                endCallBack()
            }
            .start()
    }
    fun checkerDeathAnimation(endCallBack: () -> Unit){
        checkerView.animate().setDuration(300).scaleX(0F).scaleY(0F).withEndAction(endCallBack).start()
    }


}