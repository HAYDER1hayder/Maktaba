package com.ElOuedUniv.maktaba.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import kotlinx.serialization.Transient

@Serializable
data class Book(
    val isbn: String,
    val title: String,
    @SerialName("nbpages")
    val nbPages: Int,

    @SerialName("pagesread")
    val pagesRead: Int = 0,

    @SerialName("imageurl")
    val imageUrl: String? = null,
    val status: String = "Reading"
) {

    @Transient
    val progress: Float
        get() = if (nbPages > 0) pagesRead.toFloat() / nbPages.toFloat() else 0f



    @Transient
    val pagesRemaining: Int
        get() = (nbPages - pagesRead).coerceAtLeast(0)
}