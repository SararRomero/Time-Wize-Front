package com.example.timewize.iu.screens

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.RectF
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import com.example.timewize.R
import kotlin.math.min

class AdminDashboardActivity : AppCompatActivity() {

    private lateinit var txtActiveUsers: TextView
    private lateinit var txtUsedCategories: TextView
    private lateinit var txtGeneralReports: TextView
    private lateinit var cardHeader: CardView
    private lateinit var cardGraphs: CardView

    // Mock data for weekly productivity (7 days)
    private val weeklyData = listOf(60f, 75f, 55f, 80f, 90f, 70f, 85f)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        // Views
        txtActiveUsers = findViewById(R.id.txtActiveUsers)
        txtUsedCategories = findViewById(R.id.txtUsedCategories)
        txtGeneralReports = findViewById(R.id.txtGeneralReports)
        cardHeader = findViewById(R.id.cardHeader)
        cardGraphs = findViewById(R.id.cardGraphs)

        // Put mock values (you'll replace with backend values later)
        txtActiveUsers.text = "42"
        txtUsedCategories.text = "8"
        txtGeneralReports.text = "156 reportes analizados"

        // Animate header & cards
        animateHeader()
        animateCards()

        // Replace placeholder View (id weeklyChart in XML) with our custom WeeklyChartView
        // (In the XML we used a View with id weeklyChart; here we find it and replace with custom view)
        val placeholder = findViewById<View?>(R.id.weeklyChart)
        if (placeholder != null) {
            val parent = placeholder.parent
            if (parent is ViewGroup) {
                val index = parent.indexOfChild(placeholder)
                parent.removeViewAt(index)

                val chartView = WeeklyChartView(this).apply {
                    layoutParams = ViewGroup.LayoutParams(
                        (220 * resources.displayMetrics.density).toInt(),
                        (220 * resources.displayMetrics.density).toInt()
                    )
                    // give the view some padding so it looks centered in the card
                    setPadding(8, 8, 8, 8)
                    // set data
                    setData(weeklyData)
                }

                parent.addView(chartView, index)
                // simple entrance animation
                chartView.alpha = 0f
                chartView.translationY = 30f
                chartView.animate().alpha(1f).translationY(0f).setDuration(600).start()
            }
        }

        // Click sample on reports card (if user taps it)
        val reportsCard = findViewById<CardView?>(R.id.cardGraphs) // reuse cardGraphs for demo
        reportsCard?.setOnClickListener {
            Toast.makeText(this, "Abrir: Reportes generales (demo)", Toast.LENGTH_SHORT).show()
        }
    }

    private fun animateHeader() {
        cardHeader.translationY = -180f
        cardHeader.alpha = 0f

        val animY = ObjectAnimator.ofFloat(cardHeader, View.TRANSLATION_Y, 0f)
        val animA = ObjectAnimator.ofFloat(cardHeader, View.ALPHA, 1f)
        AnimatorSet().apply {
            playTogether(animY, animA)
            duration = 600
            interpolator = OvershootInterpolator(0.8f)
            start()
        }
    }

    private fun animateCards() {
        cardGraphs.alpha = 0f
        cardGraphs.translationY = 80f
        cardGraphs.animate()
            .alpha(1f)
            .translationY(0f)
            .setStartDelay(250)
            .setDuration(650)
            .setInterpolator(OvershootInterpolator())
            .start()
    }

    /**
     * Small custom View that draws a simple weekly bar chart.
     * - No external libs
     * - Lightweight and performant for demo / delivery
     */
    class WeeklyChartView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0
    ) : View(context, attrs, defStyle) {

        private var values: List<Float> = emptyList()
        private val paintBar = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            color = 0xFF1E80FF.toInt() // blue
        }
        private val paintGrid = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 2f
            color = 0x22000000
        }
        private val paintText = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = 0xFF333333.toInt()
            textSize = 26f * resources.displayMetrics.density / 2f
        }
        private val rect = RectF()

        fun setData(list: List<Float>) {
            values = list
            invalidate()
        }

        override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
            // default to suggested size if wrap_content
            val minSide = min(suggestedMinimumWidth, suggestedMinimumHeight)
            val w = resolveSize(minSide.coerceAtLeast(200), widthMeasureSpec)
            val h = resolveSize(minSide.coerceAtLeast(200), heightMeasureSpec)
            setMeasuredDimension(w, h)
        }

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)

            val paddingLeft = paddingLeft.toFloat()
            val paddingRight = paddingRight.toFloat()
            val paddingTop = paddingTop.toFloat()
            val paddingBottom = paddingBottom.toFloat()

            val w = width - paddingLeft - paddingRight
            val h = height - paddingTop - paddingBottom

            // Draw subtle horizontal grid lines (3)
            for (i in 0..3) {
                val y = paddingTop + h * i / 3f
                canvas.drawLine(paddingLeft, y, paddingLeft + w, y, paintGrid)
            }

            if (values.isEmpty()) return

            val max = (values.maxOrNull() ?: 100f).coerceAtLeast(1f)
            val barCount = values.size
            val gap = (w * 0.12f) / (barCount + 1)
            val totalGap = gap * (barCount + 1)
            val barAreaWidth = w - totalGap
            val barWidth = barAreaWidth / barCount

            // draw bars
            for ((i, v) in values.withIndex()) {
                val left = paddingLeft + gap * (i + 1) + i * barWidth
                val right = left + barWidth
                val norm = v / max
                val top = paddingTop + (1f - norm) * h
                rect.set(left, top, right, paddingTop + h)
                canvas.drawRoundRect(rect, 8f, 8f, paintBar)

                // small label below each bar (day)
                val label = when (i) {
                    0 -> "L"
                    1 -> "M"
                    2 -> "M"
                    3 -> "J"
                    4 -> "V"
                    5 -> "S"
                    6 -> "D"
                    else -> ""
                }
                val tx = left + (barWidth - paintText.measureText(label)) / 2f
                val ty = paddingTop + h + 24f
                canvas.drawText(label, tx, ty, paintText)
            }
        }
    }
}
