package com.example.timewize.iu.screens

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.min

class CircularProgressView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private val backgroundPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.LTGRAY
        style = Paint.Style.STROKE
        strokeWidth = 8f
    }

    private val progressPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
        color = Color.BLUE
        style = Paint.Style.STROKE
        strokeWidth = 8f
        strokeCap = Paint.Cap.ROUND
    }

    private val rectF = RectF()
    private var progress = 0f

    fun setProgress(progress: Float) {
        this.progress = progress
        invalidate()
    }

    fun setProgressColor(color: Int) {
        progressPaint.color = color
        invalidate()
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val centerX = width / 2f
        val centerY = height / 2f
        val radius = min(centerX, centerY) - 8f

        rectF.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius)

        // Dibujar círculo de fondo
        canvas.drawArc(rectF, 0f, 360f, false, backgroundPaint)

        // Dibujar progreso
        val sweepAngle = 360 * progress / 100f
        canvas.drawArc(rectF, -90f, sweepAngle, false, progressPaint)
    }
}