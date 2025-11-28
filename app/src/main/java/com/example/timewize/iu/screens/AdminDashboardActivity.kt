package com.example.timewize.iu.screens

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Context
import android.content.Intent
import android.graphics.*
import android.os.Bundle
import android.util.AttributeSet
import android.view.View
import android.view.ViewGroup
import android.view.animation.OvershootInterpolator
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
import com.example.timewize.R
import com.example.timewize.iu.screens.categories.CategoriesActivity
import com.example.timewize.iu.screens.UserReportsActivity
import kotlin.math.min
import kotlin.math.cos
import kotlin.math.sin

class AdminDashboardActivity : AppCompatActivity() {

    // Views del dashboard
    private lateinit var txtActiveUsers: TextView
    private lateinit var txtUsedCategories: TextView
    private lateinit var txtTotalTimeSummary: TextView
    private lateinit var txtTopCategory: TextView
    private lateinit var txtProductivityTrend: TextView
    private lateinit var txtActivityInsights: TextView
    private lateinit var cardHeader: CardView
    private lateinit var cardTimeDistribution: CardView
    private lateinit var cardProductivityTrend: CardView

    // Views del navbar
    private lateinit var navAdminHome: ImageView
    private lateinit var navCategories: ImageView
    private lateinit var navAddAdmin: ImageButton
    private lateinit var navReports: ImageView
    private lateinit var navAdminProfile: ImageView

    // Mock data
    private val timeDistributionData = listOf(
        PieChartData("Trabajo", 40f, Color.parseColor("#FF6B6B")),
        PieChartData("Estudio", 25f, Color.parseColor("#4ECDC4")),
        PieChartData("Ocio", 20f, Color.parseColor("#45B7D1")),
        PieChartData("Otros", 15f, Color.parseColor("#96CEB4"))
    )

    private val productivityTrendData = listOf(65f, 72f, 68f, 75f, 82f, 78f, 85f)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)

        initViews()
        setupBottomNavigation()
        setupMockData()
        setupCharts()
        animateViews()
        setupClickListeners()
    }

    private fun initViews() {
        try {
            // Views del dashboard
            txtActiveUsers = findViewById(R.id.txtActiveUsers)
            txtUsedCategories = findViewById(R.id.txtUsedCategories)
            txtTotalTimeSummary = findViewById(R.id.txtTotalTimeSummary)
            txtTopCategory = findViewById(R.id.txtTopCategory)
            txtProductivityTrend = findViewById(R.id.txtProductivityTrend)
            txtActivityInsights = findViewById(R.id.txtActivityInsights)
            cardHeader = findViewById(R.id.cardHeader)
            cardTimeDistribution = findViewById(R.id.cardTimeDistribution)
            cardProductivityTrend = findViewById(R.id.cardProductivityTrend)

            // Views del navbar
            navAdminHome = findViewById(R.id.navAdminHome)
            navCategories = findViewById(R.id.navCategories)
            navAddAdmin = findViewById(R.id.navAddAdmin)
            navReports = findViewById(R.id.navReports)
            navAdminProfile = findViewById(R.id.navAdminProfile)

        } catch (e: Exception) {
            Toast.makeText(this, "Error inicializando views: ${e.message}", Toast.LENGTH_LONG).show()
        }
    }

    private fun setupBottomNavigation() {
        // Inicio Admin (ya estamos aquí)
        navAdminHome.setOnClickListener {
            updateNavigationIcons(R.id.navAdminHome)
            Toast.makeText(this, "Dashboard", Toast.LENGTH_SHORT).show()
        }

        // Categorías Globales
        navCategories.setOnClickListener {
            navigateToCategories()
            updateNavigationIcons(R.id.navCategories)
        }

        // ✅ BOTÓN CENTRAL + - GESTIÓN DE USUARIOS
        navAddAdmin.setOnClickListener {
            navigateToUsersManagement()
            updateNavigationIcons(R.id.navAddAdmin)
        }

        // Reportes Generales - CORREGIDO ✅
        navReports.setOnClickListener {
            try {
                val intent = Intent(this, UserReportsActivity::class.java)
                startActivity(intent)
                applySlideTransition()
                updateNavigationIcons(R.id.navReports)
            } catch (e: Exception) {
                Toast.makeText(this, "Error abriendo reportes: ${e.message}", Toast.LENGTH_LONG).show()
                e.printStackTrace()
            }
        }

        // Perfil Admin
        navAdminProfile.setOnClickListener {
            Toast.makeText(this, "Perfil Admin", Toast.LENGTH_SHORT).show()
            updateNavigationIcons(R.id.navAdminProfile)
        }

        // Establecer el dashboard como seleccionado por defecto
        updateNavigationIcons(R.id.navAdminHome)
    }

    // ✅ MÉTODO PARA NAVEGAR A GESTIÓN DE USUARIOS
    private fun navigateToUsersManagement() {
        try {
            val intent = Intent(this, AdminUsersActivity::class.java)
            startActivity(intent)
            applySlideTransition()
        } catch (e: Exception) {
            Toast.makeText(this, "Error abriendo gestión de usuarios: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    private fun navigateToCategories() {
        try {
            val intent = Intent(this, CategoriesActivity::class.java)
            startActivity(intent)
            applySlideTransition()
        } catch (e: Exception) {
            Toast.makeText(this, "Error abriendo categorías: ${e.message}", Toast.LENGTH_LONG).show()
            e.printStackTrace()
        }
    }

    // ✅ MÉTODO CORREGIDO - SIN DEPRECATED WARNING
    private fun applySlideTransition() {
        try {
            // Usar overrideActivityTransition en lugar de overridePendingTransition
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                overrideActivityTransition(OVERRIDE_TRANSITION_OPEN, R.anim.slide_in_right, R.anim.slide_out_left)
            } else {
                // Fallback para versiones anteriores
                @Suppress("DEPRECATION")
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left)
            }
        } catch (e: Exception) {
            // Si falla, continuar sin animación
        }
    }

    private fun updateNavigationIcons(selectedItemId: Int) {
        val unselectedColor = ContextCompat.getColor(this, R.color.text_secondary)
        val selectedColor = ContextCompat.getColor(this, R.color.colorPrimary)

        // Resetear todos los tintes
        navAdminHome.setColorFilter(unselectedColor)
        navCategories.setColorFilter(unselectedColor)
        navAddAdmin.setColorFilter(unselectedColor)
        navReports.setColorFilter(unselectedColor)
        navAdminProfile.setColorFilter(unselectedColor)

        // Aplicar color primario al ítem seleccionado
        when (selectedItemId) {
            R.id.navAdminHome -> navAdminHome.setColorFilter(selectedColor)
            R.id.navCategories -> navCategories.setColorFilter(selectedColor)
            R.id.navAddAdmin -> navAddAdmin.setColorFilter(selectedColor)
            R.id.navReports -> navReports.setColorFilter(selectedColor)
            R.id.navAdminProfile -> navAdminProfile.setColorFilter(selectedColor)
        }
    }

    private fun setupMockData() {
        if (::txtActiveUsers.isInitialized) txtActiveUsers.text = "156"
        if (::txtUsedCategories.isInitialized) txtUsedCategories.text = "24"
        if (::txtTotalTimeSummary.isInitialized) txtTotalTimeSummary.text = "24 horas"
        if (::txtTopCategory.isInitialized) txtTopCategory.text = "Trabajo"
        if (::txtProductivityTrend.isInitialized) txtProductivityTrend.text = "+12.5%"
        if (::txtActivityInsights.isInitialized) {
            txtActivityInsights.text = "• 156 usuarios activos esta semana\n• 24 categorías diferentes en uso\n• Distribución equilibrada de tiempo\n• Tendencia positiva en productividad"
        }
    }

    private fun setupCharts() {
        setupTimeDistributionChart()
        setupProductivityTrendChart()
    }

    private fun setupTimeDistributionChart() {
        val chartContainer = findViewById<ViewGroup?>(R.id.chartTimeByCategory)
        if (chartContainer != null) {
            val pieChart = PieChartView(this).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                setData(timeDistributionData)
            }
            chartContainer.addView(pieChart)

            pieChart.alpha = 0f
            pieChart.scaleX = 0.8f
            pieChart.scaleY = 0.8f
            pieChart.animate()
                .alpha(1f)
                .scaleX(1f)
                .scaleY(1f)
                .setDuration(600)
                .start()
        }
    }

    private fun setupProductivityTrendChart() {
        val chartContainer = findViewById<ViewGroup?>(R.id.chartProductivityTrend)
        if (chartContainer != null) {
            val lineChart = LineChartView(this).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )
                setData(productivityTrendData)
            }
            chartContainer.addView(lineChart)

            lineChart.alpha = 0f
            lineChart.translationY = 30f
            lineChart.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(600)
                .start()
        }
    }

    private fun animateViews() {
        if (::cardHeader.isInitialized) animateHeader()
        animateCards()
    }

    private fun animateHeader() {
        cardHeader.translationY = -180f
        cardHeader.alpha = 0f

        AnimatorSet().apply {
            playTogether(
                ObjectAnimator.ofFloat(cardHeader, View.TRANSLATION_Y, 0f),
                ObjectAnimator.ofFloat(cardHeader, View.ALPHA, 1f)
            )
            duration = 600
            interpolator = OvershootInterpolator(0.8f)
            start()
        }
    }

    private fun animateCards() {
        val cards = listOfNotNull(
            if (::cardTimeDistribution.isInitialized) cardTimeDistribution else null,
            if (::cardProductivityTrend.isInitialized) cardProductivityTrend else null
        )

        cards.forEachIndexed { index, card ->
            card.alpha = 0f
            card.translationY = 80f
            card.animate()
                .alpha(1f)
                .translationY(0f)
                .setStartDelay(250 + (index * 100).toLong())
                .setDuration(650)
                .setInterpolator(OvershootInterpolator())
                .start()
        }
    }

    private fun setupClickListeners() {
        if (::cardTimeDistribution.isInitialized) {
            cardTimeDistribution.setOnClickListener {
                Toast.makeText(this, "Ver detalles de distribución de tiempo", Toast.LENGTH_SHORT).show()
            }
        }

        if (::cardProductivityTrend.isInitialized) {
            cardProductivityTrend.setOnClickListener {
                Toast.makeText(this, "Ver análisis de productividad", Toast.LENGTH_SHORT).show()
            }
        }
    }

    // Data class y Custom Views
    data class PieChartData(
        val label: String,
        val value: Float,
        val color: Int
    )

    class PieChartView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0
    ) : View(context, attrs, defStyle) {

        private var data: List<PieChartData> = emptyList()
        private val paint = Paint(Paint.ANTI_ALIAS_FLAG)
        private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.WHITE
            textSize = 12f * resources.displayMetrics.density
            textAlign = Paint.Align.CENTER
        }
        private val rect = RectF()

        fun setData(newData: List<PieChartData>) {
            data = newData
            invalidate()
        }

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)

            if (data.isEmpty()) return

            val centerX = width / 2f
            val centerY = height / 2f
            val radius = min(centerX, centerY) * 0.8f

            rect.set(centerX - radius, centerY - radius, centerX + radius, centerY + radius)

            val total = data.sumOf { it.value.toDouble() }.toFloat()
            var startAngle = -90f

            data.forEach { item ->
                val sweepAngle = (item.value / total) * 360f

                paint.color = item.color
                canvas.drawArc(rect, startAngle, sweepAngle, true, paint)

                // Texto en el centro de cada segmento
                val midAngle = startAngle + sweepAngle / 2
                val textRadius = radius * 0.6f
                val x = centerX + textRadius * cos(Math.toRadians(midAngle.toDouble())).toFloat()
                val y = centerY + textRadius * sin(Math.toRadians(midAngle.toDouble())).toFloat()

                if (sweepAngle > 30f) {
                    val percentage = (item.value / total * 100).toInt()
                    canvas.drawText("$percentage%", x, y, textPaint)
                }

                startAngle += sweepAngle
            }
        }
    }

    class LineChartView @JvmOverloads constructor(
        context: Context,
        attrs: AttributeSet? = null,
        defStyle: Int = 0
    ) : View(context, attrs, defStyle) {

        private var values: List<Float> = emptyList()
        private val linePaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 4f
            color = Color.parseColor("#2196F3")
        }
        private val pointPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.FILL
            color = Color.parseColor("#2196F3")
        }
        private val gridPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            style = Paint.Style.STROKE
            strokeWidth = 1f
            color = Color.parseColor("#22000000")
        }
        private val textPaint = Paint(Paint.ANTI_ALIAS_FLAG).apply {
            color = Color.parseColor("#666666")
            textSize = 10f * resources.displayMetrics.density
            textAlign = Paint.Align.CENTER
        }

        fun setData(newValues: List<Float>) {
            values = newValues
            invalidate()
        }

        override fun onDraw(canvas: Canvas) {
            super.onDraw(canvas)

            if (values.isEmpty()) return

            val paddingLeft = paddingLeft.toFloat()
            val paddingRight = paddingRight.toFloat()
            val paddingTop = paddingTop.toFloat()
            val paddingBottom = paddingBottom.toFloat()

            val w = width - paddingLeft - paddingRight
            val h = height - paddingTop - paddingBottom

            // Draw grid
            for (i in 0..4) {
                val y = paddingTop + (h * i / 4f)
                canvas.drawLine(paddingLeft, y, paddingLeft + w, y, gridPaint)
            }

            val maxValue = values.maxOrNull() ?: 100f
            val pointRadius = 6f

            // Draw line and points
            val path = Path()
            values.forEachIndexed { index, value ->
                val x = paddingLeft + (w * index / (values.size - 1))
                val y = paddingTop + h - (value / maxValue * h)

                if (index == 0) {
                    path.moveTo(x, y)
                } else {
                    path.lineTo(x, y)
                }

                canvas.drawCircle(x, y, pointRadius, pointPaint)
            }

            canvas.drawPath(path, linePaint)

            // Draw day labels
            val days = listOf("Lun", "Mar", "Mié", "Jue", "Vie", "Sáb", "Dom")
            values.forEachIndexed { index, _ ->
                val x = paddingLeft + (w * index / (values.size - 1))
                val y = paddingTop + h + 25f
                val day = days.getOrNull(index) ?: ""
                canvas.drawText(day, x, y, textPaint)
            }
        }
    }
}