package io.erksn.portfolio.data.api.model

data class Project(
    val id: String,
    val title: String,
    val imageUrl: String,
    val textColor: String,
    val backgroundColor: String,
    val appUrl: String?,
    val tagline: String?
)