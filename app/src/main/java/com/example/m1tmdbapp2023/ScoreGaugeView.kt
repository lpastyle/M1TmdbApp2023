package com.example.m1tmdbapp2023

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.graphics.drawable.Drawable
import android.text.TextPaint
import android.util.AttributeSet
import android.view.View
import kotlin.math.abs


class ScoreGaugeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // hold XML custom attributes
    private var scoreMax:Int
    private var scoreValue:Float = 75f
    private var scoreLabel:String = "Preview"
    private var scoreColor:Int = Color.RED

    // Paint styles used for rendering are initialized here to improve performance,
    // since onDraw() is called for every screen refresh.
    private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style = Paint.Style.FILL
        color = Color.DKGRAY
        textSize = 55.0f
        typeface = Typeface.create("", Typeface.BOLD)
    }

    // compute dimensions to be used for drawing text
    private val fontMetrics = textPaint.fontMetrics
    private val textHeight = abs(textPaint.fontMetrics.ascent + fontMetrics.descent)

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ScoreGaugeView)
        scoreMax = typedArray.getInt(R.styleable.ScoreGaugeView_scoreMax,100)
        scoreValue = typedArray.getFloat(R.styleable.ScoreGaugeView_scoreValue, 0f)
        scoreLabel = typedArray.getString(R.styleable.ScoreGaugeView_scoreLabel)                  .toString()
        scoreColor = typedArray.getColor(R.styleable.ScoreGaugeView_scoreColor, 0)
        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        // requested width and height
        val reqWidth = MeasureSpec.getSize(widthMeasureSpec)
        val reqHeight = MeasureSpec.getSize(heightMeasureSpec)

        // your choice
        val desiredWidth: Int =  reqWidth
        val desiredHeight: Int = (textHeight * 2.5f).toInt()

        val width = when (MeasureSpec.getMode(widthMeasureSpec)) {
            MeasureSpec.EXACTLY -> reqWidth
            MeasureSpec.UNSPECIFIED -> desiredWidth
            else -> Math.min(reqWidth, desiredWidth) // AT_MOST condition
        }

        val height = when (MeasureSpec.getMode(heightMeasureSpec)) {
            MeasureSpec.EXACTLY -> reqHeight
            MeasureSpec.UNSPECIFIED -> desiredHeight
            else -> Math.min(reqHeight, desiredHeight) // AT_MOST condition
        }

        // set the width and the height of the view
        setMeasuredDimension(width, height)
    }

}