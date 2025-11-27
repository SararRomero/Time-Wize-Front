package com.example.timewize.iu.screens

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.app.Activity
import android.widget.ImageView
import androidx.core.content.ContextCompat
import com.example.timewize.R

object NavbarHelper {

    fun setupNavbar(activity: Activity) {
        val home = activity.findViewById<ImageView>(R.id.navHome)
        val calendar = activity.findViewById<ImageView>(R.id.navCalendar)
        val add = activity.findViewById<ImageView>(R.id.navAdd)
        val notifications = activity.findViewById<ImageView>(R.id.navNotifications)
        val profile = activity.findViewById<ImageView>(R.id.navProfile)

        val icons = listOf(home, calendar, notifications, profile)
        val defaultColor = ContextCompat.getColor(activity, R.color.text_secondary)
        val selectedColor = ContextCompat.getColor(activity, R.color.primary_blue)

        fun animateTint(icon: ImageView, isSelected: Boolean) {
            val startColor = if (isSelected) defaultColor else selectedColor
            val endColor = if (isSelected) selectedColor else defaultColor
            val animator = ValueAnimator.ofObject(ArgbEvaluator(), startColor, endColor)
            animator.duration = 200
            animator.addUpdateListener {
                icon.setColorFilter(it.animatedValue as Int)
            }
            animator.start()
        }

        fun animateBounce(icon: ImageView) {
            icon.animate()
                .scaleX(1.15f)
                .scaleY(1.15f)
                .setDuration(120)
                .withEndAction {
                    icon.animate()
                        .scaleX(1f)
                        .scaleY(1f)
                        .setDuration(120)
                        .start()
                }
                .start()
        }

        fun animateGlow(icon: ImageView) {
            val animator = ValueAnimator.ofFloat(0f, 1f, 0f)
            animator.duration = 350

            animator.addUpdateListener {
                val value = it.animatedValue as Float
                icon.alpha = 0.7f + (value * 0.3f)
            }
            animator.start()
        }

        icons.forEach { icon ->
            icon.setOnClickListener {
                icons.forEach { i -> animateTint(i, i == icon) }

                animateBounce(icon)
                animateGlow(icon)

                when (icon.id) {
                    R.id.navHome -> { /* Navegar a Home */ }
                    R.id.navCalendar -> { /* Navegar */ }
                    R.id.navNotifications -> { /* Navegar */ }
                    R.id.navProfile -> { /* Navegar */ }
                }
            }
        }

        add.setOnClickListener {
            // Acción del botón central
        }
    }
}
