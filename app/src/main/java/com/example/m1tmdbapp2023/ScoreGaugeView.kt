package com.example.m1tmdbapp2023

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View


class ScoreGaugeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // hold XML custom attributes
    private var scoreMax:Int = 100
    private var scoreValue:Float = 75f
    private var scoreLabel:String = "Preview"
    private var scoreColor:Int = Color.RED

    init {

    }

}