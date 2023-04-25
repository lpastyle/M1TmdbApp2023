package com.example.m1tmdbapp2023

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.util.AttributeSet
import android.util.Log
import android.view.View
import kotlin.math.abs
import kotlin.math.min

class ScoreGaugeView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val LOGTAG = ScoreGaugeView::class.simpleName

    // hold XML custom attributes
    private var scoreMax:Int
    private var scoreValue:Float
    private var scoreLabel:String?
    private var scoreColor:Int

    // Paint styles used for rendering are initialized here to improve performance,
    // since onDraw() is called for every screen refresh.
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        textSize = context.resources.getDimension(R.dimen.score_gauge_view_label)
        typeface = Typeface.create(null as String?, Typeface.BOLD)
    }

    // compute dimensions to be used for drawing text
    private val fontMetrics = paint.fontMetrics
    private val textHeight = abs(paint.fontMetrics.ascent + fontMetrics.descent)

    //  stroke width for rectangles
    private val so = 4f

    init {
        val typedArray = context.obtainStyledAttributes(attrs, R.styleable.ScoreGaugeView,defStyleAttr,0)
        scoreMax = typedArray.getInt(R.styleable.ScoreGaugeView_scoreMax, 100)
        scoreValue = typedArray.getFloat(R.styleable.ScoreGaugeView_scoreValue, 75f)
        scoreLabel = typedArray.getString(R.styleable.ScoreGaugeView_scoreLabel)
        if (scoreLabel == null) scoreLabel = if (isInEditMode) context.getString(R.string.no_label) else ""
        scoreColor = typedArray.getColor(R.styleable.ScoreGaugeView_scoreColor, Color.GREEN)
        typedArray.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        // super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        // requested width and height
        val reqWidth = MeasureSpec.getSize(widthMeasureSpec)
        val reqHeight = MeasureSpec.getSize(heightMeasureSpec)

        // your choice
        val desiredWidth: Int = reqWidth
        val desiredHeight: Int = (textHeight * 2.5).toInt()

        val width = when (MeasureSpec.getMode(widthMeasureSpec)) {
            MeasureSpec.EXACTLY -> reqWidth
            MeasureSpec.UNSPECIFIED -> desiredWidth
            else -> min(reqWidth, desiredWidth) // AT_MOST condition
        }
        val height = when (MeasureSpec.getMode(heightMeasureSpec)) {
            MeasureSpec.EXACTLY -> reqHeight
            MeasureSpec.UNSPECIFIED -> desiredHeight
            else -> min(reqHeight, desiredHeight) // AT_MOST condition
        }

        // set the width and the height of the view
        if (isInEditMode) {
            setMeasuredDimension(desiredWidth, desiredHeight)
        } else {
            setMeasuredDimension(width, height)
        }
    }

    // useless for our need
    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        Log.d(LOGTAG,"onSizeChanged($w,$h,$oldw,$oldh)")
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)

        val percent = if (scoreMax > 0) scoreValue / scoreMax else 0f

        // Draw gauge main rectangle
        paint.style= Paint.Style.FILL
        paint.color=scoreColor
        canvas?.drawRect(
            paddingLeft.toFloat(),
            paddingTop.toFloat(),
            (width- paddingRight) * percent,
            height.toFloat() - paddingBottom,
            paint)

        // Draw stroke on main rectangle
        paint.style= Paint.Style.STROKE
        paint.strokeWidth = so * 1.5f
        paint.color= Color.BLACK
        canvas?.drawRect(
            paddingLeft.toFloat() + so,
            paddingTop.toFloat() + so,
            width.toFloat() - paddingRight - so,
            height.toFloat() - paddingBottom - so,
            paint)

        //val textWidth = textPaint.measureText(scoreLabel)
        paint.color = Color.DKGRAY
        paint.style = Paint.Style.FILL
        paint.textAlign = Paint.Align.LEFT
        canvas?.drawText(
            " $scoreLabel",
            paddingLeft.toFloat(),
            (paddingTop + height - paddingBottom + textHeight) * 0.5f,
            paint)

        paint.textAlign = Paint.Align.RIGHT
        canvas?.drawText((100 * percent).toInt().toString() +  "% ",
            width - paddingRight.toFloat(),
            (paddingTop + height - paddingBottom + textHeight) * 0.5f,
            paint)

        // debug lines to show canvas footprint
        //canvas?.drawLine(0f,height * 0.5f, width.toFloat(), height * 0.5f, rectPaint)
        //canvas?.drawLine(width * 0.5f, 0f, width*0.5f, height.toFloat(), rectPaint)
    }

    fun updateScore(label:String, color: Int, value:Float, max:Int) {
        scoreLabel = label
        scoreColor = color
        scoreValue = value
        scoreMax  = max
        invalidate()
    }

}


/*
 * --- JAVA Style way of implementing constructors ---
 *
class ScoreGaugeView : View {

    constructor(context: Context) : super(context) {
        init(null, 0)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(attrs, 0)
    }

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context,
        attrs,
        defStyle
    ) {
        init(attrs, defStyle)
    }

    fun init (attrs: AttributeSet?, defStyle: Int) {
        val typedArray =
            context.obtainStyledAttributes(attrs, R.styleable.ScoreGaugeView, defStyle, 0)
        scoreMax = typedArray.getInt(R.styleable.ScoreGaugeView_scoreMax, 100)
        scoreValue = typedArray.getFloat(R.styleable.ScoreGaugeView_scoreValue, 0f)
        scoreLabel = typedArray.getString(R.styleable.ScoreGaugeView_scoreLabel).toString()
        scoreColor = typedArray.getColor(R.styleable.ScoreGaugeView_scoreColor, 0)
        typedArray.recycle()

    }
*/