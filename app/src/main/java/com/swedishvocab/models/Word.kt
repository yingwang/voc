package com.swedishvocab.models

data class Word(
    val swedish: String,
    val english: String,
    val category: String,
    val audioUrl: String? = null,
    val phonetic: String? = null
)

data class Dictionary(
    val words: List<Word>,
    val source: String? = null,
    val license: String? = null,
    val totalEntries: Int? = null
)
