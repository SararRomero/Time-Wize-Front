package com.example.timewize.iu.screens.categories

data class Category(
    val name: String,
    val iconRes: Int,
    val color: String = "",
    val taskCount: Int = 0
)