package com.example.m1tmdbapp2023

import android.content.Context
import android.content.res.TypedArray
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Typeface
import android.hardware.SensorEvent
import android.util.AttributeSet
import android.view.View
import kotlin.math.abs
import kotlin.math.cos
import kotlin.math.min
import kotlin.math.sin

class SensorDemoView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    // constants
    private val DIAL_SCALE = 0.9f

    // hold XML custom attributes
    private var dialCaption: String? = null
    private var dialColor = Color.BLUE // default color
    private val dialNeedleColors: Array<String> = context.resources.getStringArray(R.array.dial_needle_colors)

    // Paint styles used for rendering are initialized here to improve performance,
    // since onDraw() is called for every screen refresh.
    private var paint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        strokeWidth = 4f
        textAlign = Paint.Align.CENTER
        color = dialColor
        textSize = context.resources.getDimension(R.dimen.sensor_view_caption);
        typeface = Typeface.create(null as String?, Typeface.NORMAL)
    }

    // compute dimensions to be used for drawing text
    private val fontMetrics = paint.fontMetrics
    private val textHeight = abs(paint.fontMetrics.ascent + fontMetrics.descent)
    private val captionHeight = textHeight * 2f

    private var sensorEvent: SensorEvent? = null

    init {
        // Load attributes
        val a: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.SensorDemoView, defStyleAttr, 0)
        dialCaption = a.getString(R.styleable.SensorDemoView_dialCaption)
        if (dialCaption == null) dialCaption = if (isInEditMode) context.getString(R.string.sensor_unknown) else ""
        dialColor = a.getColor(R.styleable.SensorDemoView_dialColor, dialColor)
        a.recycle()
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        //super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        // requested width and height
        val reqWidth = MeasureSpec.getSize(widthMeasureSpec)
        val reqHeight = MeasureSpec.getSize(heightMeasureSpec)

        // your choice
        val desiredWidth: Int = reqWidth
        val desiredHeight: Int = captionHeight.toInt() + reqHeight

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

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        // compute circle radius and center coordinates
        val cx = measuredWidth * 0.5f
        val cy = (measuredHeight + captionHeight) * 0.5f
        val radius = min(measuredWidth.toFloat(), measuredHeight - captionHeight) * 0.5f * DIAL_SCALE

        // draw the dial circle
        paint.style = Paint.Style.STROKE
        paint.color = dialColor
        canvas.drawCircle(cx, cy, radius, paint)

        // draw the dial needles
        sensorEvent?.let {
            for (i in 0..min(it.values.size, dialNeedleColors.size) -1) {
                paint.color = dialNeedleColors[i].toInt()
                val angle = ((it.values[i] * 2 * Math.PI) / it.sensor.getMaximumRange())
                canvas.drawLine(
                    cx,
                    cy,
                    cx + radius * sin(angle).toFloat(),
                    cy - radius * cos(angle).toFloat(),
                    paint
                )

            }
        }

        // draw dial caption
        paint.style = Paint.Style.FILL
        paint.color = dialColor
        canvas.drawText(
            dialCaption!!,
            cx,
            textHeight*2,
            paint
        )
    }

    fun setCaption(caption: String) {
        dialCaption = caption
        invalidate()
    }

    fun setSensorEvent(event: SensorEvent?) {
        this.sensorEvent = event
        invalidate()
    }


}