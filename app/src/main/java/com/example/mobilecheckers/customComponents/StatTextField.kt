package com.example.mobilecheckers.customComponents

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.LinearLayout
import android.widget.TextView
import com.example.mobilecheckers.R

class StatTextField @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : LinearLayout(context, attrs, defStyleAttr) {

    private var textView: TextView

    init {
        // Подключаем XML-разметку
        LayoutInflater.from(context).inflate(R.layout.stat_text_field, this, true)

        // Находим TextView
        textView = findViewById(R.id.textField)

        // Читаем атрибуты из XML
        attrs?.let {
            val typedArray = context.obtainStyledAttributes(it, R.styleable.StatTextField)
            val text = typedArray.getString(R.styleable.StatTextField_textFieldText) ?: "Заглушка"
            typedArray.recycle()

            textView.text = text
        }
    }

    // Метод для изменения текста программно
    fun setTextFieldText(text: String) {
        textView.text = text
    }
    //Метод для изменения счетчика
    fun setCounter(text: String){
        textView = findViewById(R.id.numberField)
        textView.text = text

    }
}
